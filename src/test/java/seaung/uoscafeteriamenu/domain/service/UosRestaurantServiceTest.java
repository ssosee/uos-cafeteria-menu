package seaung.uoscafeteriamenu.domain.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheMember;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheUosRestaurant;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheMemberRepository;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheUosRestaurantRepository;
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

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static seaung.uoscafeteriamenu.domain.entity.UosRestaurantName.*;
import static seaung.uoscafeteriamenu.domain.entity.UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE;

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
    @Autowired
    CacheManager cacheManager;
    @Autowired
    CacheMemberRepository cacheMemberRepository;
    @Autowired
    CacheUosRestaurantRepository cacheUosRestaurantRepository;
    @Autowired
    EntityManager em;

    @Autowired
    RedisTemplate<?, ?> redisTemplate;

    @AfterEach
    void tearDown() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @DisplayName("캐시에 학교식당의 금일 조식 메뉴가 없는 경우 데이터베이스에서 학교식당의 금일 조식 메뉴를 조회하고 조회수를 1증가하고, 캐시에 데이터를 저장한다.")
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

        CacheUosRestaurant cacheUosRestaurant = cacheUosRestaurantRepository.findById(uosRestaurant.getId().toString()).get();
        assertAll(
                () -> assertThat(cacheUosRestaurant.getMenuDesc()).isEqualTo("라면"),
                () -> assertThat(cacheUosRestaurant.getRestaurantName()).isEqualTo(STUDENT_HALL),
                () -> assertThat(cacheUosRestaurant.getMealType()).isEqualTo(MealType.BREAKFAST),
                () -> assertThat(cacheUosRestaurant.getView()).isEqualTo(1),
                () -> assertThat(cacheUosRestaurant.getLikeCount()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("캐시에 학교 메뉴가 있으면 캐시에 있는 메뉴의 조회수를 1증가한다.")
    void getUosRestaurantMenuInCache() {
        // given
        String date = CrawlingUtils.toDateString(LocalDateTime.now());
        UosRestaurant uosRestaurant = createUosRestaurant(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        uosRestaurantRepository.save(uosRestaurant);
        cacheUosRestaurantRepository.save(CacheUosRestaurant.of(uosRestaurant));

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
    @DisplayName("캐시에 금일 조식 메뉴가 없는 경우 데이터베이스에서 금일 조식 메뉴들을 조회하고 조회수를 1증가하고, 캐시에 데이터를 저장한다.")
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

        List<CacheUosRestaurant> cacheUosRestaurants = cacheUosRestaurantRepository.findByDateAndMealType(date, MealType.BREAKFAST);
        assertThat(cacheUosRestaurants).hasSize(4)
                .extracting("restaurantName", "mealType", "menuDesc", "view", "likeCount")
                .contains(
                        tuple(UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면", 1, 0),
                        tuple(UosRestaurantName.MAIN_BUILDING, MealType.BREAKFAST, "김밥", 1, 0),
                        tuple(UosRestaurantName.WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스", 1, 0),
                        tuple(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 1, 0)
                );
    }

    @Test
    @DisplayName("캐시에서 금일 조식 메뉴들을 조회하고 조회수를 1증가한다.")
    void getUosRestaurantMenusInCache() {
        // given
        String date = CrawlingUtils.toDateString(LocalDateTime.now());

        UosRestaurant uosRestaurant1 = createUosRestaurant(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, UosRestaurantName.MAIN_BUILDING, MealType.BREAKFAST, "김밥", 0, 0);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, UosRestaurantName.WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스", 0, 0);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 0, 0);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));
        cacheUosRestaurantRepository.saveAll(List.of(CacheUosRestaurant.of(uosRestaurant1),
                CacheUosRestaurant.of(uosRestaurant2),
                CacheUosRestaurant.of(uosRestaurant3),
                CacheUosRestaurant.of(uosRestaurant4)));

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
    @DisplayName("학식메뉴를 추천할 때 캐시에 메뉴가 있으면 동기화를 한 후 추천수가 1증가하고 추천이력이 생성되고 캐시에 저장된다.")
    void recommendUosRestaurantMenuSyncCache() {
        // given
        String date = CrawlingUtils.toDateString(LocalDateTime.now());

        UosRestaurant uosRestaurant = createUosRestaurant(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, 0);
        uosRestaurantRepository.save(uosRestaurant);

        String botUserId = "1";
        Member member = Member.create(botUserId, 0L);
        memberRepository.save(member);
        cacheMemberRepository.save(CacheMember.of(member));

        RecommendUosRestaurantMenuInput input = createRecommendUosRestaurantMenuInput(botUserId, date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST);

        uosRestaurant.changeViewAndLikeCount(100, 0);
        cacheUosRestaurantRepository.save(CacheUosRestaurant.of(uosRestaurant));

        // when
        String response = uosRestaurantService.recommendUosRestaurantMenu(input);

        // then
        Member findMember = memberRepository.findByBotUserId(botUserId).get();
        UosRestaurant findUosRestaurant = uosRestaurantRepository.findById(uosRestaurant.getId()).get();
        MenuLike findMenuLike = menuLikeRepository.findByMemberIdAndUosRestaurantId(findMember.getId(), findUosRestaurant.getId()).get();

        assertAll(
                () -> assertThat(response).isEqualTo(UosRestaurantService.RECOMMEND_MESSAGE),
                () -> assertThat(findUosRestaurant.getLikeCount()).isEqualTo(1),
                () -> assertThat(findUosRestaurant.getView()).isEqualTo(100),
                () -> assertThat(findMember).isNotNull(),
                () -> assertThat(findMenuLike).isNotNull(),
                () -> assertThat(findMenuLike.getMember()).isEqualTo(findMember),
                () -> assertThat(findMenuLike.getUosRestaurant()).isEqualTo(findUosRestaurant)
        );

        CacheUosRestaurant cacheUosRestaurant = cacheUosRestaurantRepository
                .findByDateAndRestaurantNameAndMealType(date, STUDENT_HALL, MealType.BREAKFAST).get();
        assertAll(
                () ->assertThat(cacheUosRestaurant).isNotNull(),
                () -> assertThat(cacheUosRestaurant.getLikeCount()).isEqualTo(1),
                () -> assertThat(cacheUosRestaurant.getView()).isEqualTo(100)
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
        Member member = Member.create(botUserId, 0L);
        memberRepository.save(member);
        cacheMemberRepository.save(CacheMember.of(member));

        MenuLike menuLike = MenuLike.create(member, uosRestaurant);
        menuLikeRepository.save(menuLike);

        RecommendUosRestaurantMenuInput input = createRecommendUosRestaurantMenuInput(botUserId, date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST);

        // when // then
        assertThatThrownBy(() -> uosRestaurantService.recommendUosRestaurantMenu(input))
                .isInstanceOf(MenuLikeException.class)
                .hasMessage(MenuLikeException.CONFLICT_MENU);
    }

    @Test
    @DisplayName("아침메뉴 중 가장 조회수가 많은 메뉴 1개를 캐시에서 조회한다.(조회수가 같으면 추천수가 많은 것을 조회)")
    void findTop1BREAKFASTUosRestaurantMenuByViewOfCacheHit() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 10, 59, 59);
        String date = CrawlingUtils.toDateString(now);

        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.BREAKFAST, "김밥", 1, 0);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스", 2, 0);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 2, 1);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));
        cacheUosRestaurantRepository.saveAll(List.of(CacheUosRestaurant.of(uosRestaurant1),
                CacheUosRestaurant.of(uosRestaurant2),
                CacheUosRestaurant.of(uosRestaurant3),
                CacheUosRestaurant.of(uosRestaurant4)));

        Pageable pageable = PageRequest.of(0, 1);

        // when
        Page<UosRestaurantMenuResponse> result = uosRestaurantService.findTop1UosRestaurantMenuByView(pageable, now);

        // then
        assertThat(result).hasSize(1)
                .extracting("restaurantName", "mealType", "menu", "view", "likeCount")
                .contains(
                        tuple(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.getKrName(), MealType.BREAKFAST.getKrName(), "제육", 3, 1)
                );


        List<CacheUosRestaurant> cacheUosRestaurants = cacheUosRestaurantRepository.findByDateAndMealType(date, MealType.BREAKFAST);
        CacheUosRestaurant cacheUosRestaurant = uosRestaurantService.findTop1MenuByViewInCache(cacheUosRestaurants).get();

        assertAll(
                () -> assertThat(cacheUosRestaurant.getRestaurantName()).isEqualTo(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE),
                () -> assertThat(cacheUosRestaurant.getMealType()).isEqualTo(MealType.BREAKFAST),
                () -> assertThat(cacheUosRestaurant.getMenuDesc()).isEqualTo("제육"),
                () -> assertThat(cacheUosRestaurant.getView()).isEqualTo(3),
                () -> assertThat(cacheUosRestaurant.getLikeCount()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("아침메뉴 중 가장 조회수가 많은 메뉴 1개를 조회하고 캐시에 저장한다.(조회수가 같으면 추천수가 많은 것을 조회)")
    void findTop1BREAKFASTUosRestaurantMenuByView() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 10, 59, 59);
        String date = CrawlingUtils.toDateString(now);

        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.BREAKFAST, "김밥", 1, 0);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스", 2, 0);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 2, 1);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));

        Pageable pageable = PageRequest.of(0, 1);

        // when
        Page<UosRestaurantMenuResponse> result = uosRestaurantService.findTop1UosRestaurantMenuByView(pageable, now);

        // then
        assertThat(result).hasSize(1)
                .extracting("restaurantName", "mealType", "menu", "view", "likeCount")
                .contains(
                        tuple(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.getKrName(), MealType.BREAKFAST.getKrName(), "제육", 3, 1)
                );


        List<CacheUosRestaurant> cacheUosRestaurants = cacheUosRestaurantRepository.findByDateAndMealType(date, MealType.BREAKFAST);
        CacheUosRestaurant cacheUosRestaurant = uosRestaurantService.findTop1MenuByViewInCache(cacheUosRestaurants).get();

        assertAll(
                () -> assertThat(cacheUosRestaurant.getRestaurantName()).isEqualTo(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE),
                () -> assertThat(cacheUosRestaurant.getMealType()).isEqualTo(MealType.BREAKFAST),
                () -> assertThat(cacheUosRestaurant.getMenuDesc()).isEqualTo("제육"),
                () -> assertThat(cacheUosRestaurant.getView()).isEqualTo(3),
                () -> assertThat(cacheUosRestaurant.getLikeCount()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("점심메뉴 중 가장 조회수가 많은 메뉴 1개를 조회하고 캐시에 저장한다.(조회수가 같으면 추천수가 많은 것을 조회)")
    void findTop1LUNCHUosRestaurantMenuByView() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 11, 0, 0);
        String date = CrawlingUtils.toDateString(now);

        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.LUNCH, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.LUNCH, "김밥", 1, 0);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.LUNCH, "돈까스", 2, 0);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.LUNCH, "제육", 2, 1);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));

        Pageable pageable = PageRequest.of(0, 1);

        // when
        Page<UosRestaurantMenuResponse> result = uosRestaurantService.findTop1UosRestaurantMenuByView(pageable, now);

        // then
        assertThat(result).hasSize(1)
                .extracting("restaurantName", "mealType", "menu", "view", "likeCount")
                .contains(
                        tuple(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.getKrName(), MealType.LUNCH.getKrName(), "제육", 3, 1)
                );


        List<CacheUosRestaurant> cacheUosRestaurants = cacheUosRestaurantRepository.findByDateAndMealType(date, MealType.LUNCH);
        CacheUosRestaurant cacheUosRestaurant = uosRestaurantService.findTop1MenuByViewInCache(cacheUosRestaurants).get();

        assertAll(
                () -> assertThat(cacheUosRestaurant.getRestaurantName()).isEqualTo(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE),
                () -> assertThat(cacheUosRestaurant.getMealType()).isEqualTo(MealType.LUNCH),
                () -> assertThat(cacheUosRestaurant.getMenuDesc()).isEqualTo("제육"),
                () -> assertThat(cacheUosRestaurant.getView()).isEqualTo(3),
                () -> assertThat(cacheUosRestaurant.getLikeCount()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("저녁메뉴 중 가장 조회수가 많은 메뉴 1개를 조회하고 캐시에 저장한다.(조회수가 같으면 추천수가 많은 것을 조회)")
    void findTop1DINNERUosRestaurantMenuByView() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 14, 0, 0);
        String date = CrawlingUtils.toDateString(now);

        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.DINNER, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.DINNER, "김밥", 1, 0);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.DINNER, "돈까스", 2, 0);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.DINNER, "제육", 2, 1);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));

        Pageable pageable = PageRequest.of(0, 1);

        // when
        Page<UosRestaurantMenuResponse> result = uosRestaurantService.findTop1UosRestaurantMenuByView(pageable, now);

        // then
        assertThat(result).hasSize(1)
                .extracting("restaurantName", "mealType", "menu", "view", "likeCount")
                .contains(
                        tuple(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.getKrName(), MealType.DINNER.getKrName(), "제육", 3, 1)
                );


        List<CacheUosRestaurant> cacheUosRestaurants = cacheUosRestaurantRepository.findByDateAndMealType(date, MealType.DINNER);
        CacheUosRestaurant cacheUosRestaurant = uosRestaurantService.findTop1MenuByViewInCache(cacheUosRestaurants).get();

        assertAll(
                () -> assertThat(cacheUosRestaurant.getRestaurantName()).isEqualTo(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE),
                () -> assertThat(cacheUosRestaurant.getMealType()).isEqualTo(MealType.DINNER),
                () -> assertThat(cacheUosRestaurant.getMenuDesc()).isEqualTo("제육"),
                () -> assertThat(cacheUosRestaurant.getView()).isEqualTo(3),
                () -> assertThat(cacheUosRestaurant.getLikeCount()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("데이터베이스에 인기 메뉴가 없으면 예외가 발생한다.")
    void findTop1UosRestaurantMenuByViewExceptionNOT_FOUND_MENU() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 14, 0, 0);

        Pageable pageable = PageRequest.of(0, 1);
        MealType mealType = CrawlingUtils.localDateTimeToMealType(now);

        // when // then
        assertThatThrownBy(() -> uosRestaurantService.findTop1UosRestaurantMenuByView(pageable, now))
                .isInstanceOf(UosRestaurantMenuException.class)
                .hasMessage(String.format(UosRestaurantMenuException.NOT_FOUND_MENU_FORMAT, mealType.getKrName()));
    }

    @Test
    @DisplayName("아침메뉴 중 가장 추천수가 많은 메뉴 1개를 캐시에서 조회한다.(추천수가 같으면 조회수가 많은 것을 조회)")
    void findTop1BREAKFASTUosRestaurantMenuByLikeCountOfCacheHit2() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 10, 59, 59);
        String date = CrawlingUtils.toDateString(now);

        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.BREAKFAST, "김밥", 1, 1);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스", 11, 0);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 1, 3);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));
        cacheUosRestaurantRepository.saveAll(List.of(CacheUosRestaurant.of(uosRestaurant1),
                CacheUosRestaurant.of(uosRestaurant2),
                CacheUosRestaurant.of(uosRestaurant3),
                CacheUosRestaurant.of(uosRestaurant4)));


        Pageable pageable = PageRequest.of(0, 1);

        // when
        Page<UosRestaurantMenuResponse> result = uosRestaurantService.findTop1UosRestaurantMenuByLikeCount(pageable, now);

        // then
        assertThat(result).hasSize(1)
                .extracting("restaurantName", "mealType", "menu", "view", "likeCount")
                .contains(
                        tuple(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.getKrName(), MealType.BREAKFAST.getKrName(), "제육", 2, 3)
                );

        List<CacheUosRestaurant> cacheUosRestaurants = cacheUosRestaurantRepository.findByDateAndMealType(date, MealType.BREAKFAST);
        CacheUosRestaurant cacheUosRestaurant = uosRestaurantService.findTop1MenuByLikeCountInCache(cacheUosRestaurants).get();

        assertAll(
                () -> assertThat(cacheUosRestaurant.getRestaurantName()).isEqualTo(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE),
                () -> assertThat(cacheUosRestaurant.getMealType()).isEqualTo(MealType.BREAKFAST),
                () -> assertThat(cacheUosRestaurant.getMenuDesc()).isEqualTo("제육"),
                () -> assertThat(cacheUosRestaurant.getView()).isEqualTo(2),
                () -> assertThat(cacheUosRestaurant.getLikeCount()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("아침메뉴 중 가장 추천수가 많은 메뉴 1개를 캐시에서 조회한다.(추천수가 같으면 조회수가 많은 것을 조회)")
    void findTop1BREAKFASTUosRestaurantMenuByLikeCountOfCacheHit() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 10, 59, 59);
        String date = CrawlingUtils.toDateString(now);

        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.BREAKFAST, "김밥", 1, 1);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스", 2, 2);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 3, 2);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));
        cacheUosRestaurantRepository.saveAll(List.of(CacheUosRestaurant.of(uosRestaurant1),
                CacheUosRestaurant.of(uosRestaurant2),
                CacheUosRestaurant.of(uosRestaurant3),
                CacheUosRestaurant.of(uosRestaurant4)));


        Pageable pageable = PageRequest.of(0, 1);

        // when
        Page<UosRestaurantMenuResponse> result = uosRestaurantService.findTop1UosRestaurantMenuByLikeCount(pageable, now);

        // then
        assertThat(result).hasSize(1)
                .extracting("restaurantName", "mealType", "menu", "view", "likeCount")
                .contains(
                        tuple(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.getKrName(), MealType.BREAKFAST.getKrName(), "제육", 4, 2)
                );

        List<CacheUosRestaurant> cacheUosRestaurants = cacheUosRestaurantRepository.findByDateAndMealType(date, MealType.BREAKFAST);
        CacheUosRestaurant cacheUosRestaurant = uosRestaurantService.findTop1MenuByLikeCountInCache(cacheUosRestaurants).get();

        assertAll(
                () -> assertThat(cacheUosRestaurant.getRestaurantName()).isEqualTo(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE),
                () -> assertThat(cacheUosRestaurant.getMealType()).isEqualTo(MealType.BREAKFAST),
                () -> assertThat(cacheUosRestaurant.getMenuDesc()).isEqualTo("제육"),
                () -> assertThat(cacheUosRestaurant.getView()).isEqualTo(4),
                () -> assertThat(cacheUosRestaurant.getLikeCount()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("아침메뉴 중 가장 추천수가 많은 메뉴 1개를 조회한다.(추천수가 같으면 조회수가 많은 것을 조회)")
    void findTop1BREAKFASTUosRestaurantMenuByLikeCount() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 10, 59, 59);
        String date = CrawlingUtils.toDateString(now);

        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.BREAKFAST, "김밥", 1, 1);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스", 2, 2);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 3, 2);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));

        Pageable pageable = PageRequest.of(0, 1);

        // when
        Page<UosRestaurantMenuResponse> result = uosRestaurantService.findTop1UosRestaurantMenuByLikeCount(pageable, now);

        // then
        assertThat(result).hasSize(1)
                .extracting("restaurantName", "mealType", "menu", "view", "likeCount")
                .contains(
                        tuple(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.getKrName(), MealType.BREAKFAST.getKrName(), "제육", 4, 2)
                );

        List<CacheUosRestaurant> cacheUosRestaurants = cacheUosRestaurantRepository.findByDateAndMealType(date, MealType.BREAKFAST);
        CacheUosRestaurant cacheUosRestaurant = uosRestaurantService.findTop1MenuByLikeCountInCache(cacheUosRestaurants).get();

        assertAll(
                () -> assertThat(cacheUosRestaurant.getRestaurantName()).isEqualTo(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE),
                () -> assertThat(cacheUosRestaurant.getMealType()).isEqualTo(MealType.BREAKFAST),
                () -> assertThat(cacheUosRestaurant.getMenuDesc()).isEqualTo("제육"),
                () -> assertThat(cacheUosRestaurant.getView()).isEqualTo(4),
                () -> assertThat(cacheUosRestaurant.getLikeCount()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("점심메뉴 중 가장 추천수가 많은 메뉴 1개를 조회한다.(추천수가 같으면 조회수가 많은 것을 조회)")
    void findTop1LUNCHUosRestaurantMenuByLikeCount() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 11, 0, 0);
        String date = CrawlingUtils.toDateString(now);

        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.LUNCH, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.LUNCH, "김밥", 1, 1);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.LUNCH, "돈까스", 2, 2);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.LUNCH, "제육", 3, 2);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));

        Pageable pageable = PageRequest.of(0, 1);

        // when
        Page<UosRestaurantMenuResponse> result = uosRestaurantService.findTop1UosRestaurantMenuByLikeCount(pageable, now);

        // then
        assertThat(result).hasSize(1)
                .extracting("restaurantName", "mealType", "menu", "view", "likeCount")
                .contains(
                        tuple(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.getKrName(), MealType.LUNCH.getKrName(), "제육", 4, 2)
                );


        List<CacheUosRestaurant> cacheUosRestaurants = cacheUosRestaurantRepository.findByDateAndMealType(date, MealType.LUNCH);
        CacheUosRestaurant cacheUosRestaurant = uosRestaurantService.findTop1MenuByLikeCountInCache(cacheUosRestaurants).get();

        assertAll(
                () -> assertThat(cacheUosRestaurant.getRestaurantName()).isEqualTo(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE),
                () -> assertThat(cacheUosRestaurant.getMealType()).isEqualTo(MealType.LUNCH),
                () -> assertThat(cacheUosRestaurant.getMenuDesc()).isEqualTo("제육"),
                () -> assertThat(cacheUosRestaurant.getView()).isEqualTo(4),
                () -> assertThat(cacheUosRestaurant.getLikeCount()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("저녁메뉴 중 가장 추천수가 많은 메뉴 1개를 조회한다.(추천수가 같으면 조회수가 많은 것을 조회)")
    void findTop1DINNERUosRestaurantMenuByLikeCount() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 14, 0, 0);
        String date = CrawlingUtils.toDateString(now);

        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.DINNER, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.DINNER, "김밥", 1, 1);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.DINNER, "돈까스", 2, 2);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.DINNER, "제육", 3, 2);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));

        Pageable pageable = PageRequest.of(0, 1);

        // when
        Page<UosRestaurantMenuResponse> result = uosRestaurantService.findTop1UosRestaurantMenuByLikeCount(pageable, now);

        // then
        assertThat(result).hasSize(1)
                .extracting("restaurantName", "mealType", "menu", "view", "likeCount")
                .contains(
                        tuple(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.getKrName(), MealType.DINNER.getKrName(), "제육", 4, 2)
                );


        List<CacheUosRestaurant> cacheUosRestaurants = cacheUosRestaurantRepository.findByDateAndMealType(date, MealType.DINNER);
        CacheUosRestaurant cacheUosRestaurant = uosRestaurantService.findTop1MenuByLikeCountInCache(cacheUosRestaurants).get();

        assertAll(
                () -> assertThat(cacheUosRestaurant.getRestaurantName()).isEqualTo(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE),
                () -> assertThat(cacheUosRestaurant.getMealType()).isEqualTo(MealType.DINNER),
                () -> assertThat(cacheUosRestaurant.getMenuDesc()).isEqualTo("제육"),
                () -> assertThat(cacheUosRestaurant.getView()).isEqualTo(4),
                () -> assertThat(cacheUosRestaurant.getLikeCount()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("데이터베이스에 추천 메뉴가 없으면 예외가 발생한다.")
    void findTop1UosRestaurantMenuByLikeCountExceptionNOT_FOUND_MENU() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 14, 0, 0);
        Pageable pageable = PageRequest.of(0, 1);

        MealType mealType = CrawlingUtils.localDateTimeToMealType(now);

        // when // then
        assertThatThrownBy(() -> uosRestaurantService.findTop1UosRestaurantMenuByLikeCount(pageable, now))
                .isInstanceOf(UosRestaurantMenuException.class)
                .hasMessage(String.format(UosRestaurantMenuException.NOT_FOUND_MENU_FORMAT, mealType.getKrName()));
    }

    @Test
    @DisplayName("운영시간이 아니면 조회수가 많은 메뉴 1개를 조회할때 예외가 발생한다.")
    void findTop1UosRestaurantMenuByViewException() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 18, 30, 0);
        Pageable pageable = PageRequest.of(0, 1);

        // when // then
        assertThatThrownBy(() -> uosRestaurantService.findTop1UosRestaurantMenuByView(pageable, now))
                .isInstanceOf(UosRestaurantMenuException.class)
                .hasMessage(UosRestaurantMenuException.CLOSED);
    }

    @Test
    @DisplayName("운영시간이 아니면 추천수가 많은 메뉴 1개를 조회할때 예외가 발생한다.")
    void findTop1UosRestaurantMenuByLikeCountException() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 18, 30, 0);
        Pageable pageable = PageRequest.of(0, 1);

        // when // then
        assertThatThrownBy(() -> uosRestaurantService.findTop1UosRestaurantMenuByLikeCount(pageable, now))
                .isInstanceOf(UosRestaurantMenuException.class)
                .hasMessage(UosRestaurantMenuException.CLOSED);
    }

    @Test
    @DisplayName("캐시에 있는 학교 식당 정보를 데이터베이스와 동기화 한다.")
    void syncCacheUosRestaurantToDatabaseUosRestaurant() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 18, 30, 0);
        String date = CrawlingUtils.toDateString(now);

        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.DINNER, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.DINNER, "김밥", 0, 0);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.DINNER, "돈까스", 0, 0);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.DINNER, "제육", 0, 0);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));

        em.flush();
        em.clear();

        uosRestaurant2.changeViewAndLikeCount(2, 2);
        uosRestaurant3.changeViewAndLikeCount(3, 3);
        uosRestaurant4.changeViewAndLikeCount(4, 4);
        cacheUosRestaurantRepository.saveAll(List.of(CacheUosRestaurant.of(uosRestaurant1),
                CacheUosRestaurant.of(uosRestaurant2),
                CacheUosRestaurant.of(uosRestaurant3),
                CacheUosRestaurant.of(uosRestaurant4)));

        // when
        uosRestaurantService.syncCacheUosRestaurantToDatabaseUosRestaurant(now);

        // then
        List<UosRestaurant> findUosRestaurants = uosRestaurantRepository.findByCrawlingDate(date);
        assertThat(findUosRestaurants).hasSize(4)
                .extracting("restaurantName", "mealType", "menuDesc", "view", "likeCount")
                .containsAnyOf(
                        tuple(UosRestaurantName.STUDENT_HALL, MealType.DINNER, "라면", 0, 0),
                        tuple(UosRestaurantName.MAIN_BUILDING, MealType.DINNER, "김밥", 2, 2),
                        tuple(UosRestaurantName.WESTERN_RESTAURANT, MealType.DINNER, "돈까스", 3, 3),
                        tuple(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE, MealType.DINNER, "제육", 4, 4)
                );
    }

    @Test
    @DisplayName("금일 캐시된 학식메뉴를 삭제하고 내일 학식메뉴를 데이터베이스에서 조회하여 캐시에 warm-up 한다.")
    void deleteAllCacheUosRestaurantAndWarmUpNextDayUosRestaurant() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 18, 30, 0);
        String current = CrawlingUtils.toDateString(now);

        LocalDateTime future = LocalDateTime.of(2023, 8, 17, 18, 30, 0);
        String next = CrawlingUtils.toDateString(future);

        UosRestaurant uosRestaurant1 = createUosRestaurant(current, STUDENT_HALL, MealType.DINNER, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(current, MAIN_BUILDING, MealType.DINNER, "김밥", 0, 0);
        UosRestaurant uosRestaurant3 = createUosRestaurant(current, WESTERN_RESTAURANT, MealType.DINNER, "돈까스", 0, 0);
        UosRestaurant uosRestaurant4 = createUosRestaurant(current, MUSEUM_OF_NATURAL_SCIENCE, MealType.DINNER, "제육", 0, 0);
        UosRestaurant uosRestaurant5 = createUosRestaurant(next, STUDENT_HALL, MealType.DINNER, "떡라면", 0, 0);
        UosRestaurant uosRestaurant6 = createUosRestaurant(next, MAIN_BUILDING, MealType.DINNER, "충무김밥", 0, 0);
        UosRestaurant uosRestaurant7 = createUosRestaurant(next, WESTERN_RESTAURANT, MealType.DINNER, "치즈돈까스", 0, 0);
        UosRestaurant uosRestaurant8 = createUosRestaurant(next, MUSEUM_OF_NATURAL_SCIENCE, MealType.DINNER, "김치제육", 0, 0);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4,
                uosRestaurant5, uosRestaurant6, uosRestaurant7, uosRestaurant8));

        cacheUosRestaurantRepository.saveAll(CacheUosRestaurant.ofList(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4)));

        // when
        uosRestaurantService.deleteAllCacheUosRestaurantAndWarmUpNextDayUosRestaurant(now);

        // then
        List<CacheUosRestaurant> cache = cacheUosRestaurantRepository.findByDate(next);
        assertThat(cache).hasSize(4)
                .extracting("restaurantName", "mealType", "menuDesc", "view", "likeCount")
                .containsAnyOf(
                        tuple(UosRestaurantName.STUDENT_HALL, MealType.DINNER, "떡라면", 0, 0),
                        tuple(UosRestaurantName.MAIN_BUILDING, MealType.DINNER, "충무김밥", 0, 0),
                        tuple(UosRestaurantName.WESTERN_RESTAURANT, MealType.DINNER, "치즈돈까스", 0, 0),
                        tuple(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE, MealType.DINNER, "김치제육", 0, 0)
                );
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