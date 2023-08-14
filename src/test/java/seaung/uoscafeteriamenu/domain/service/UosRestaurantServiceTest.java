package seaung.uoscafeteriamenu.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.entity.*;
import seaung.uoscafeteriamenu.domain.repository.MemberRepository;
import seaung.uoscafeteriamenu.domain.repository.MenuLikeRepository;
import seaung.uoscafeteriamenu.domain.repository.UosRestaurantRepository;
import seaung.uoscafeteriamenu.domain.service.request.RecommendUosRestaurantMenuInput;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantsInput;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
import seaung.uoscafeteriamenu.web.exception.MenuLikeException;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UosRestaurantServiceTest {

    @Autowired
    UosRestaurantService uosRestaurantService;
    @Autowired
    UosRestaurantRepository uosRestaurantRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MenuLikeRepository menuLikeRepository;

    @Test
    @DisplayName("학교식당의 금일 조식 메뉴를 조회하고 조회수를 1증가한다.")
    void getUosRestaurantMenu() {
        // given
        String date = CrawlingUtils.toDateString(LocalDateTime.now());
        UosRestaurant uosRestaurant = createUosRestaurant(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        uosRestaurantRepository.save(uosRestaurant);

        UosRestaurantInput input = createUosRestaurantInput(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST);

        // when
        UosRestaurantMenuResponse uosRestaurantMenu = uosRestaurantService.getUosRestaurantMenu(input);

        // then
        assertAll(
                () -> assertThat(uosRestaurantMenu.getMenu()).isEqualTo("라면"),
                () -> assertThat(uosRestaurantMenu.getRestaurantName()).isEqualTo("학생회관 1층"),
                () -> assertThat(uosRestaurantMenu.getMealType()).isEqualTo("조식"),
                () -> assertThat(uosRestaurantMenu.getView()).isEqualTo(1),
                () -> assertThat(uosRestaurantMenu.getLikeCount()).isEqualTo(0)
        );
    }


    @Test
    @DisplayName("학교식당의 메뉴가 없을 경우 예외가 발생한다.")
    void getUosRestaurantMenuUosRestaurantMenuException() {
        // given
        String date = CrawlingUtils.toDateString(LocalDateTime.now());
        UosRestaurantInput input = createUosRestaurantInput(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST);

        // when // then
        assertThatThrownBy(() -> uosRestaurantService.getUosRestaurantMenu(input))
                .isInstanceOf(UosRestaurantMenuException.class)
                .hasMessage(UosRestaurantMenuException.NOT_FOUND_MENU);
    }

    @Test
    @DisplayName("금일 조식 메뉴들을 조회하고 조회수를 1증가한다.")
    void getUosRestaurantMenus() {
        // given
        String date = CrawlingUtils.toDateString(LocalDateTime.now());

        UosRestaurant uosRestaurant1 = createUosRestaurant(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, UosRestaurantName.MAIN_BUILDING, MealType.BREAKFAST, "김밥", 0, 0);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, UosRestaurantName.WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스", 0, 0);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 0, 0);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));

        UosRestaurantsInput input = createUosRestaurantsInput(date, MealType.BREAKFAST);

        // when
        List<UosRestaurantMenuResponse> uosRestaurantMenus = uosRestaurantService.getUosRestaurantsMenu(input);

        // then
        assertThat(uosRestaurantMenus).hasSize(4)
                .extracting("restaurantName", "mealType", "menu", "view", "likeCount")
                .contains(
                        tuple(UosRestaurantName.STUDENT_HALL.getKrName(), MealType.BREAKFAST.getKrName(), "라면", 1, 0),
                        tuple(UosRestaurantName.MAIN_BUILDING.getKrName(), MealType.BREAKFAST.getKrName(), "김밥", 1, 0),
                        tuple(UosRestaurantName.WESTERN_RESTAURANT.getKrName(), MealType.BREAKFAST.getKrName(), "돈까스", 1, 0),
                        tuple(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.getKrName(), MealType.BREAKFAST.getKrName(), "제육", 1, 0)
                );

    }

    @Test
    @DisplayName("사용자가 학식 메뉴를 추천할 때 회원이 없으면 회원이 생성되고 추천수가 1증가하고 추천이력이 생성된다.")
    void recommendUosRestaurantMenu() {
        // given
        String date = CrawlingUtils.toDateString(LocalDateTime.now());

        UosRestaurant uosRestaurant = createUosRestaurant(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, 0);
        uosRestaurantRepository.save(uosRestaurant);

        String botUserId = "1";
        RecommendUosRestaurantMenuInput input = createRecommendUosRestaurantMenuInput(botUserId, date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST);

        // when
        String response = uosRestaurantService.recommendUosRestaurantMenu(input);

        // then
        Member findMember = memberRepository.findByBotUserId(botUserId).get();
        UosRestaurant findUosRestaurant = uosRestaurantRepository.findById(uosRestaurant.getId()).get();
        MenuLike findMenuLike = menuLikeRepository.findByMemberIdAndUosRestaurantId(findMember.getId(), findUosRestaurant.getId()).get();

        assertAll(
                () -> assertThat(response).isEqualTo("추천 고맙다. 내친.구.휴.먼"),
                () -> assertThat(findUosRestaurant.getLikeCount()).isEqualTo(1),
                () -> assertThat(findMember).isNotNull(),
                () -> assertThat(findMenuLike).isNotNull(),
                () -> assertThat(findMenuLike.getMember()).isEqualTo(findMember),
                () -> assertThat(findMenuLike.getUosRestaurant()).isEqualTo(findUosRestaurant)
        );
    }

    @Test
    @DisplayName("사용자가 학식 메뉴를 추천할 때 사용자의 추천이력이 있으면 예외가 발생한다.")
    void recommendUosRestaurantMenuException() {
        // given
        String date = CrawlingUtils.toDateString(LocalDateTime.now());

        UosRestaurant uosRestaurant = createUosRestaurant(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, 0);
        uosRestaurantRepository.save(uosRestaurant);

        String botUserId = "1";
        Member member = Member.create(botUserId);
        memberRepository.save(member);

        MenuLike menuLike = MenuLike.create(member, uosRestaurant);
        menuLikeRepository.save(menuLike);

        RecommendUosRestaurantMenuInput input = createRecommendUosRestaurantMenuInput(botUserId, date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST);

        // when // then
        assertThatThrownBy(() -> uosRestaurantService.recommendUosRestaurantMenu(input))
                .isInstanceOf(MenuLikeException.class)
                .hasMessage(MenuLikeException.CONFLICT_MENU);
    }

    private RecommendUosRestaurantMenuInput createRecommendUosRestaurantMenuInput(String botUserId, String date, UosRestaurantName restaurantName, MealType mealType) {
        return RecommendUosRestaurantMenuInput.builder()
                .botUserId(botUserId)
                .date(date)
                .restaurantName(restaurantName)
                .mealType(mealType)
                .build();
    }

    private UosRestaurantsInput createUosRestaurantsInput(String date, MealType mealType) {
        return UosRestaurantsInput.builder()
                .date(date)
                .mealType(mealType)
                .build();
    }

    private UosRestaurant createUosRestaurant(String date, UosRestaurantName uosRestaurantName, MealType mealType,
                                              String menu, Integer view, Integer likeCount) {
        return UosRestaurant.builder()
                .crawlingDate(date)
                .restaurantName(uosRestaurantName)
                .mealType(mealType)
                .menuDesc(menu)
                .view(view)
                .likeCount(likeCount)
                .build();
    }

    private UosRestaurant createUosRestaurant(String date, UosRestaurantName uosRestaurantName, MealType mealType, Integer likeCount) {
        return UosRestaurant.builder()
                .crawlingDate(date)
                .restaurantName(uosRestaurantName)
                .mealType(mealType)
                .likeCount(likeCount)
                .build();
    }

    private UosRestaurantInput createUosRestaurantInput(String date, UosRestaurantName uosRestaurantName, MealType mealType) {
        return UosRestaurantInput.builder()
                .date(date)
                .restaurantName(uosRestaurantName)
                .mealType(mealType)
                .build();
    }
}