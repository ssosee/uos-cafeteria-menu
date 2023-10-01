package seaung.uoscafeteriamenu.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UosRestaurantService {

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
                .findById(CacheUosRestaurant.createId(input));

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

        // 학식 조회
        UosRestaurant findUosRestaurant = uosRestaurantRepository.findByCrawlingDateAndRestaurantNameAndMealType(input.getDate(),
                        input.getRestaurantName(), input.getMealType())
                .orElseThrow(() -> new UosRestaurantMenuException(UosRestaurantMenuException.NOT_FOUND_MENU));

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

        return "추천 고맙다! 내 친구 휴.먼";
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

        // 캐시에서 인기 메뉴 조회
        Page<CacheUosRestaurant> findTop1MenuInCache = cacheUosRestaurantRepository
                .findByDateAndMealTypeOrderByViewDescLikeCountDesc(pageable, date, mealType);

        // 캐시에 인기 메뉴가 있으면
        if(!findTop1MenuInCache.getContent().isEmpty()) {
            // 조회수 증가
            findTop1MenuInCache.getContent().forEach(CacheUosRestaurant::increaseView);

            // 캐시 저장
            cacheUosRestaurantRepository.saveAll(findTop1MenuInCache);

            return UosRestaurantMenuResponse.ofPageByCacheUosRestaurant(findTop1MenuInCache);
        }

        // 인기 메뉴 조회
        Page<UosRestaurant> findMenu = uosRestaurantRepository
                .findByCrawlingDateAndMealTypeOrderByViewDescLikeCountDesc(pageable, date, mealType);

        // 인기 메뉴가 없으면
        if(findMenu.getContent().isEmpty()) {
            throw new UosRestaurantMenuException(UosRestaurantMenuException.NOT_FOUND_MENU);
        }

        // 조회수 증가
        findMenu.getContent().forEach(UosRestaurant::increaseView);

        // 캐시 저장
        cacheUosRestaurantRepository.saveAll(CacheUosRestaurant.ofList(findMenu.getContent()));

        return UosRestaurantMenuResponse.ofPageByUosRestaurant(findMenu);
    }

    // 추천수가 가장 많은 메뉴 조회(추천수가 같으면 조회수 많은 순으로 조회)
    @Transactional
    public Page<UosRestaurantMenuResponse> findTop1UosRestaurantMenuByLikeCount(Pageable pageable, LocalDateTime now) {

        // 운영시간 확인
        checkRestaurantOperationTime(now);

        MealType mealType = CrawlingUtils.localDateTimeToMealType(now);
        String date = CrawlingUtils.toDateString(now);

        // 캐시에서 추천메뉴 조회
        Page<CacheUosRestaurant> findTop1MenuInCache = cacheUosRestaurantRepository
                .findByDateAndMealTypeOrderByViewDescLikeCountDesc(pageable, date, mealType);

        if(!findTop1MenuInCache.getContent().isEmpty()) {
            // 조회수 증가
            findTop1MenuInCache.getContent().forEach(CacheUosRestaurant::increaseView);

            // 캐시 저장
            cacheUosRestaurantRepository.saveAll(findTop1MenuInCache);

            return UosRestaurantMenuResponse.ofPageByCacheUosRestaurant(findTop1MenuInCache);
        }

        // 추천 메뉴 조회
        Page<UosRestaurant> findMenu = uosRestaurantRepository.findByCrawlingDateAndMealTypeOrderByLikeCountDescViewDesc(pageable, date, mealType);

        // 추천 메뉴가 없으면
        if(findMenu.getContent().isEmpty()) {
            throw new UosRestaurantMenuException(UosRestaurantMenuException.NOT_FOUND_MENU);
        }

        // 조회수 증가
        findMenu.getContent().forEach(UosRestaurant::increaseView);

        // 캐시에 저장
        cacheUosRestaurantRepository.saveAll(CacheUosRestaurant.ofList(findMenu.getContent()));

        return UosRestaurantMenuResponse.ofPageByUosRestaurant(findMenu);
    }

    // 학교 식당 운영시간 확인
    private void checkRestaurantOperationTime(LocalDateTime now) {
        if(!OperatingTime.isOperatingTime(now)) {
            throw new UosRestaurantMenuException(UosRestaurantMenuException.CLOSED);
        }
    }

    // 캐시에 있는 학교 식당 정보를 데이터베이스와 동기화
    @Transactional
    public void syncCacheUosRestaurantToDatabaseUosRestaurant(LocalDateTime now) {
        String date = CrawlingUtils.toDateString(now);

        // 캐시에서 학교 메뉴 조회
        List<CacheUosRestaurant> findCacheUosRestaurants = cacheUosRestaurantRepository.findByDate(date);

        // 데이터베이스에서 학교 메뉴 조회
        List<UosRestaurant> findUosRestaurants = uosRestaurantRepository.findByCrawlingDate(date);
        // Map으로 변환
        Map<Long, UosRestaurant> uosMenuMap = findUosRestaurants.stream()
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
                    // Jpa 변경 감지 가능..?
                    uosMenuMap.get(db.getId()).changeViewAndLikeCount(cache.getView(), cache.getLikeCount());
                }
            }
        }

        // 캐시 삭제
        cacheUosRestaurantRepository.deleteAll();
    }
}
