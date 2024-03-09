package seaung.uoscafeteriamenu.domain.service;

import java.text.Format;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheUosRestaurant;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheUosRestaurantRepository;
import seaung.uoscafeteriamenu.domain.entity.*;
import seaung.uoscafeteriamenu.domain.repository.MemberRepository;
import seaung.uoscafeteriamenu.domain.repository.MenuLikeRepository;
import seaung.uoscafeteriamenu.domain.repository.UosRestaurantRepository;
import seaung.uoscafeteriamenu.domain.service.request.RecommendUosRestaurantMenuInput;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantsInput;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
import seaung.uoscafeteriamenu.web.exception.MemberException;
import seaung.uoscafeteriamenu.web.exception.MenuLikeException;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UosRestaurantService {

    public static final String RECOMMEND_MESSAGE = "추천 고맙다! 내 친구 휴.먼";

    private final UosRestaurantRepository uosRestaurantRepository;
    private final MenuLikeRepository menuLikeRepository;
    private final MemberRepository memberRepository;
    private final CacheUosRestaurantRepository cacheUosRestaurantRepository;

    // 학교식당의 식사종류의 학식 메뉴 조회
    // e.g) 학생회관 조식 라면
    @Transactional
    public UosRestaurantMenuResponse getUosRestaurantMenu(UosRestaurantInput input) {

        // 캐시에서 학식 조회
        Optional<CacheUosRestaurant> findCacheUosRestaurant = cacheUosRestaurantRepository
                .findByDateAndRestaurantNameAndMealType(input.getDate(), input.getRestaurantName(), input.getMealType());

        // 캐시에 학식이 존재하면
        if(findCacheUosRestaurant.isPresent()) {
            // 조회수 증가
            findCacheUosRestaurant.get().increaseView();
            CacheUosRestaurant newCacheRestaurant = cacheUosRestaurantRepository.save(findCacheUosRestaurant.get());

            return UosRestaurantMenuResponse.of(newCacheRestaurant);
        }

        // 학식 조회
        UosRestaurant findUosRestaurant = uosRestaurantRepository.findByCrawlingDateAndRestaurantNameAndMealType(input.getDate(),
                        input.getRestaurantName(), input.getMealType())
                .orElseThrow(() -> new UosRestaurantMenuException(UosRestaurantMenuException.NOT_FOUND_MENU));

        // 조회수 증가
        findUosRestaurant.increaseView();

        // 캐시에 저장
        cacheUosRestaurantRepository.save(CacheUosRestaurant.of(findUosRestaurant));

        return UosRestaurantMenuResponse.of(findUosRestaurant);
    }

    // 금일 식사종류별 학식 조회
    // [학생회관 조식 라면, 양식당 조식 돈까스, 자연과학관 조식 제육]
    @Transactional
    public List<UosRestaurantMenuResponse> getUosRestaurantsMenu(UosRestaurantsInput input) {

        // 캐시에서 학식들 조회
        List<CacheUosRestaurant> findCacheUosRestaurants = cacheUosRestaurantRepository
                .findByDateAndMealType(input.getDate(), input.getMealType());
        if(!findCacheUosRestaurants.isEmpty()) {
            // 조회수 증가
            findCacheUosRestaurants.forEach(CacheUosRestaurant::increaseView);

            // 캐시에 저장
            Iterable<CacheUosRestaurant> cacheUosRestaurants = cacheUosRestaurantRepository.saveAll(findCacheUosRestaurants);

            return StreamSupport.stream(cacheUosRestaurants.spliterator(), false)
                    .map(UosRestaurantMenuResponse::of)
                    .collect(Collectors.toList());
        }

        // 학식들 조회
        List<UosRestaurant> findUosRestaurants = uosRestaurantRepository.findByCrawlingDateAndMealType(input.getDate(), input.getMealType());

        // 조회수 증가
        findUosRestaurants.forEach(UosRestaurant::increaseView);

        // 캐시에 저장
        cacheUosRestaurantRepository.saveAll(CacheUosRestaurant.ofList(findUosRestaurants));

        return UosRestaurantMenuResponse.ofListByUosRestaurant(findUosRestaurants);
    }

    // 학식 추천하기
    @Transactional
    public String recommendUosRestaurantMenu(RecommendUosRestaurantMenuInput input) {

        UosRestaurant findUosRestaurant = getSynchronizedUosRestaurantFromCache(input);

        // 회원 조회
        Member findMember = memberRepository.findByBotUserId(input.getBotUserId())
                .orElseThrow(() -> new MemberException(MemberException.NOT_FOUND_MEMBER));

        // 추천 이력 조회
        boolean isMenuLike = menuLikeRepository
                .findByMemberIdAndUosRestaurantId(findMember.getId(), findUosRestaurant.getId())
                .isPresent();

        // 추천 이력이 있으면
        if(isMenuLike) throw new MenuLikeException(MenuLikeException.CONFLICT_MENU);

        // 추천 이력이 없으면
        increaseLikeCountAndSaveMenuLike(findUosRestaurant, findMember);

        // 캐시에 반영
        cacheUosRestaurantRepository.save(CacheUosRestaurant.of(findUosRestaurant));

        return RECOMMEND_MESSAGE;
    }

    private UosRestaurant getSynchronizedUosRestaurantFromCache(RecommendUosRestaurantMenuInput input) {
        // 캐시 조회
        Optional<CacheUosRestaurant> findCacheUosRestaurant = cacheUosRestaurantRepository
                .findByDateAndRestaurantNameAndMealType(input.getDate(), input.getRestaurantName(), input.getMealType());

        // 학식 조회
        UosRestaurant findUosRestaurant = uosRestaurantRepository.findByCrawlingDateAndRestaurantNameAndMealType(input.getDate(),
                        input.getRestaurantName(), input.getMealType())
                .orElseThrow(() -> new UosRestaurantMenuException(UosRestaurantMenuException.NOT_FOUND_MENU));

        // 캐시에 학식이 존재하면 캐시 동기화
        if(findCacheUosRestaurant.isPresent()) {
            CacheUosRestaurant cache = findCacheUosRestaurant.get();
            findUosRestaurant.changeViewAndLikeCount(cache.getView(), cache.getLikeCount());
        }

        return findUosRestaurant;
    }

    // 추천수를 증가하고 추천이력을 저장
    private void increaseLikeCountAndSaveMenuLike(UosRestaurant findUosRestaurant, Member findMember) {
        findUosRestaurant.increaseLikeCount();

        MenuLike menuLike = MenuLike.create(findMember, findUosRestaurant);
        menuLikeRepository.save(menuLike);
    }

    // 인기 메뉴 조회(조회수가 같으면 추천이 많은 순으로 조회)
    @Transactional
    public Page<UosRestaurantMenuResponse> findTop1UosRestaurantMenuByView(Pageable pageable, LocalDateTime now) {

        // 운영시간 확인
        checkRestaurantOperationTime(now);

        MealType mealType = CrawlingUtils.localDateTimeToMealType(now);
        String date = CrawlingUtils.toDateString(now);

        // 캐시에서 메뉴 조회
        List<CacheUosRestaurant> findTop1MenusInCache = cacheUosRestaurantRepository.findByDateAndMealType(date, mealType);
        // 인기 메뉴 조회
        Optional<CacheUosRestaurant> top1MenuByViewInCache = findTop1MenuByViewInCache(findTop1MenusInCache);

        // 캐시에 인기 메뉴가 있으면
        if(top1MenuByViewInCache.isPresent()) {

            CacheUosRestaurant cacheUosRestaurant = top1MenuByViewInCache.get();

            // 조회수 증가
            cacheUosRestaurant.increaseView();

            // 캐시 저장
            CacheUosRestaurant newTop1MenuInCache = cacheUosRestaurantRepository.save(cacheUosRestaurant);
            Page<CacheUosRestaurant> cacheUosRestaurants = new PageImpl<>(List.of(newTop1MenuInCache), pageable, 1);

            return UosRestaurantMenuResponse.ofPageByCacheUosRestaurant(cacheUosRestaurants);
        }

        // 인기 메뉴 조회
        Page<UosRestaurant> findMenu = uosRestaurantRepository
                .findByCrawlingDateAndMealTypeOrderByViewDescLikeCountDesc(pageable, date, mealType);

        // 인기 메뉴가 없으면
        if(findMenu.getContent().isEmpty()) {
            String exceptionMessage = String.format(
                    UosRestaurantMenuException.NOT_FOUND_MENU_FORMAT, mealType.getKrName());
            throw new UosRestaurantMenuException(exceptionMessage);
        }

        // 조회수 증가
        findMenu.getContent().forEach(UosRestaurant::increaseView);

        // 캐시 저장
        cacheUosRestaurantRepository.saveAll(CacheUosRestaurant.ofList(findMenu.getContent()));

        return UosRestaurantMenuResponse.ofPageByUosRestaurant(findMenu);
    }

    public Optional<CacheUosRestaurant> findTop1MenuByViewInCache(List<CacheUosRestaurant> findTop1MenusInCache) {
        return findTop1MenusInCache.stream()
                .max(Comparator.comparingInt(CacheUosRestaurant::getView)
                        .thenComparingInt(CacheUosRestaurant::getLikeCount));
    }

    // 추천수가 가장 많은 메뉴 조회(추천수가 같으면 조회수 많은 순으로 조회)
    @Transactional
    public Page<UosRestaurantMenuResponse> findTop1UosRestaurantMenuByLikeCount(Pageable pageable, LocalDateTime now) {

        // 운영시간 확인
        checkRestaurantOperationTime(now);

        MealType mealType = CrawlingUtils.localDateTimeToMealType(now);
        String date = CrawlingUtils.toDateString(now);

        // 캐시에서 추천메뉴 조회
        List<CacheUosRestaurant> findTop1MenusInCache = cacheUosRestaurantRepository.findByDateAndMealType(date, mealType);
        // 추천 메뉴 조회
        Optional<CacheUosRestaurant> top1MenuByLikeCountInCache = findTop1MenuByLikeCountInCache(findTop1MenusInCache);

        if(top1MenuByLikeCountInCache.isPresent()) {

            CacheUosRestaurant cacheUosRestaurant = top1MenuByLikeCountInCache.get();

            // 조회수 증가
            cacheUosRestaurant.increaseView();

            // 캐시 저장
            CacheUosRestaurant newTop1MenuInCache = cacheUosRestaurantRepository.save(cacheUosRestaurant);
            PageImpl<CacheUosRestaurant> cacheUosRestaurants = new PageImpl<>(List.of(newTop1MenuInCache), pageable, 1);

            return UosRestaurantMenuResponse.ofPageByCacheUosRestaurant(cacheUosRestaurants);
        }

        // 추천 메뉴 조회
        Page<UosRestaurant> findMenu = uosRestaurantRepository.findByCrawlingDateAndMealTypeOrderByLikeCountDescViewDesc(pageable, date, mealType);

        // 추천 메뉴가 없으면
        if(findMenu.getContent().isEmpty()) {
            String exceptionMessage = String.format(
                    UosRestaurantMenuException.NOT_FOUND_MENU_FORMAT, mealType.getKrName());
            throw new UosRestaurantMenuException(exceptionMessage);
        }

        // 조회수 증가
        findMenu.getContent().forEach(UosRestaurant::increaseView);

        // 캐시에 저장
        cacheUosRestaurantRepository.saveAll(CacheUosRestaurant.ofList(findMenu.getContent()));

        return UosRestaurantMenuResponse.ofPageByUosRestaurant(findMenu);
    }

    public Optional<CacheUosRestaurant> findTop1MenuByLikeCountInCache(List<CacheUosRestaurant> findTop1MenusInCache) {
        return findTop1MenusInCache.stream()
                .max(Comparator.comparingInt(CacheUosRestaurant::getLikeCount)
                        .thenComparingInt(CacheUosRestaurant::getView));
    }

    // 학교 식당 운영시간 확인
    private void checkRestaurantOperationTime(LocalDateTime now) {
        if(!OperatingTime.isOperatingTime(now)) {
            throw new UosRestaurantMenuException(UosRestaurantMenuException.CLOSED);
        }
    }

    // 캐시에 있는 학교 식당 정보를 데이터베이스와 동기화
    @Deprecated
    @Transactional
    public void DeprecatedSyncCacheUosRestaurantToDatabaseUosRestaurant(LocalDateTime now) {
        String date = CrawlingUtils.toDateString(now);

        // 캐시에서 학교 메뉴 조회
        List<CacheUosRestaurant> findCacheUosRestaurants = cacheUosRestaurantRepository.findByDate(date);

        // 데이터베이스에서 학교 메뉴 조회
        List<UosRestaurant> findUosRestaurants = uosRestaurantRepository.findByCrawlingDate(date);
        // Map으로 변환
        Map<Long, UosRestaurant> databaseUosMenuMap = findUosRestaurants.stream()
                .collect(Collectors.toMap(UosRestaurant::getId, f -> f));

        List<UosRestaurantMenuResponse> menuInDatabase = UosRestaurantMenuResponse.ofListByUosRestaurant(findUosRestaurants);
        List<UosRestaurantMenuResponse> menuInCache = UosRestaurantMenuResponse.ofListByCacheUosRestaurant(findCacheUosRestaurants);

        List<UosRestaurantMenuResponse> database = new ArrayList<>(menuInDatabase);

        // 캐시랑 다른 데이터베이스 리스트
        database.removeAll(menuInCache);

        // 캐시의 조회수, 추천수를 데이터베이스로 갱신
        for(UosRestaurantMenuResponse db : database) {
            for(UosRestaurantMenuResponse cache : menuInCache) {
                if(db.getId().equals(cache.getId())) {
                    databaseUosMenuMap.get(db.getId()).changeViewAndLikeCount(cache.getView(), cache.getLikeCount());
                }
            }
        }

        // 캐시 삭제
        cacheUosRestaurantRepository.deleteAll();
    }

    // 캐시에 있는 학교 메뉴 데이터베이스로 동기화
    @Transactional
    public void syncCacheUosRestaurantToDatabaseUosRestaurant(LocalDateTime now) {
        String date = CrawlingUtils.toDateString(now);

        // 캐시에서 학교 메뉴 조회
        List<CacheUosRestaurant> cacheUosRestaurants = cacheUosRestaurantRepository.findByDate(date);

        // 캐시에 학교 메뉴가 없으면 탈출
        if(cacheUosRestaurants.isEmpty()) return;

        // 캐시를 맵으로 변환
        Map<String, CacheUosRestaurant> cacheUosMenuMap = cacheUosRestaurants.stream()
                .collect(Collectors.toMap(CacheUosRestaurant::getId, c -> c));

        // 데이터베이스에서 학교 메뉴 조회
        List<UosRestaurant> databaseUosRestaurants = uosRestaurantRepository.findByCrawlingDate(date);

        // 데이터베이스와 캐시를 비교하고 캐시에만 있는 메뉴를 찾아서 데이터베이스의 메뉴에 추가하고, 캐시의 조회수와 추천수를 데이터베이스에 업데이트
        for(UosRestaurant uosRestaurant : databaseUosRestaurants) {
            CacheUosRestaurant cacheUosRestaurant = cacheUosMenuMap.get(String.valueOf(uosRestaurant.getId()));
            if(!ObjectUtils.isEmpty(cacheUosRestaurant) && !isSameUosMenuViewOrLikeCount(uosRestaurant, cacheUosRestaurant)) {
                // 동기화
                uosRestaurant.changeViewAndLikeCount(cacheUosRestaurant.getView(), cacheUosRestaurant.getLikeCount());
            }
        }
    }

    private boolean isSameUosMenuViewOrLikeCount(UosRestaurant uosRestaurant, CacheUosRestaurant cacheUosRestaurant) {
        Integer likeCount = uosRestaurant.getLikeCount();
        Integer likeCountInCache = cacheUosRestaurant.getLikeCount();
        Integer view = uosRestaurant.getView();
        Integer viewInCache = cacheUosRestaurant.getView();

        if(likeCount.equals(likeCountInCache) && view.equals(viewInCache)) {
            return true;
        }
        return false;
    }

    // 캐시에 있는 금일 학교 메뉴를 삭제하고, 내일 메뉴를 캐시에 warm-up 한다.
    public void deleteAllCacheUosRestaurantAndWarmUpNextDayUosRestaurant(LocalDateTime now) {
        // 캐시에 있는 모든 학교 메뉴 삭제
        cacheUosRestaurantRepository.deleteAll();

        LocalDateTime future = now.plusDays(1);
        String next = CrawlingUtils.toDateString(future);

        // 내일 학교 메뉴를 캐시에 warm-up
        List<UosRestaurant> findUosRestaurant = uosRestaurantRepository.findByCrawlingDate(next);
        cacheUosRestaurantRepository.saveAll(CacheUosRestaurant.ofList(findUosRestaurant));
    }
}
