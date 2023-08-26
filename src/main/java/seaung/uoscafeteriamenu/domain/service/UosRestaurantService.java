package seaung.uoscafeteriamenu.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UosRestaurantService {

    private final UosRestaurantRepository uosRestaurantRepository;
    private final MemberRepository memberRepository;
    private final MenuLikeRepository menuLikeRepository;

    // 학교식당의 식사종류의 학식 메뉴 조회
    // e.g) 학생회관 조식 라면
    @Transactional
    public UosRestaurantMenuResponse getUosRestaurantMenu(UosRestaurantInput input) {
        // 학식 조회
        UosRestaurant findUosRestaurant = uosRestaurantRepository.findByCrawlingDateAndRestaurantNameAndMealType(input.getDate(), input.getRestaurantName(), input.getMealType())
                . orElseThrow(() -> new UosRestaurantMenuException(UosRestaurantMenuException.NOT_FOUND_MENU));

        // 조회수 증가
        findUosRestaurant.increaseView();

        return UosRestaurantMenuResponse.of(findUosRestaurant);
    }

    // 금일 식사종류별 학식 조회
    // [학생회관 조식 라면, 양식당 조식 돈까스, 자연과학관 조식 제육]
    @Transactional
    public List<UosRestaurantMenuResponse> getUosRestaurantsMenu(UosRestaurantsInput input) {
        // 학식들 조회
        List<UosRestaurant> findUosRestaurants = uosRestaurantRepository.findByCrawlingDateAndAndMealType(input.getDate(), input.getMealType());

        // 조회수 증가
        findUosRestaurants.forEach(UosRestaurant::increaseView);

        return UosRestaurantMenuResponse.ofList(findUosRestaurants);
    }

    // 학식 추천하기
    @Transactional
    public String recommendUosRestaurantMenu(RecommendUosRestaurantMenuInput input) {

        // 학식 조회
        UosRestaurant findUosRestaurant = uosRestaurantRepository.findByCrawlingDateAndRestaurantNameAndMealType(input.getDate(), input.getRestaurantName(), input.getMealType())
                .orElseThrow(() -> new UosRestaurantMenuException(UosRestaurantMenuException.NOT_FOUND_MENU));

        // 회원 조회
        Member findMember = memberRepository.findByBotUserId(input.getBotUserId())
                .orElseThrow(() -> new Membe rException(MemberException.NOT_FOUND_MEMBER));

        // 추천 이력 조회
        boolean isMenuLike = menuLikeRepository
                .findByMemberIdAndUosRestaurantId(findMember.getId(), findUosRestaurant.getId())
                .isPresent();

        // 추천 이력이 있으면
        if(isMenuLike) throw new MenuLikeException(MenuLikeException.CONFLICT_MENU);

        // 추천 이력이 없으면
        increaseLikeCountAndSaveMenuLike(findUosRestaurant, findMember);

        return "추천 고맙다. 내친.구.휴.먼";
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

        // 인기 메뉴 조회
        Page<UosRestaurant> findMenu = uosRestaurantRepository.findByCrawlingDateAndMealTypeOrderByViewDescLikeCountDesc(pageable, date, mealType);

        // 조회수 증가
        findMenu.getContent().forEach(UosRestaurant::increaseView);

        return UosRestaurantMenuResponse.ofPage(findMenu);
    }

    // 추천수가 가장 많은 메뉴 조회(추천수가 같으면 조회수 많은 순으로 조회)
    @Transactional
    public Page<UosRestaurantMenuResponse> findTop1UosRestaurantMenuByLikeCount(Pageable pageable, LocalDateTime now) {

        // 운영시간 확인
        checkRestaurantOperationTime(now);

        MealType mealType = CrawlingUtils.localDateTimeToMealType(now);
        String date = CrawlingUtils.toDateString(now);

        // 추천 메뉴 조회
        Page<UosRestaurant> findMenu = uosRestaurantRepository.findByCrawlingDateAndMealTypeOrderByLikeCountDescViewDesc(pageable, date, mealType);

        // 조회수 증가
        findMenu.getContent().forEach(UosRestaurant::increaseView);

        return UosRestaurantMenuResponse.ofPage(findMenu);
    }

    private void checkRestaurantOperationTime(LocalDateTime now) {
        if(!OperatingTime.isOperatingTime(now)) {
            throw new UosRestaurantMenuException(UosRestaurantMenuException.CLOSED);
        }
    }
}
