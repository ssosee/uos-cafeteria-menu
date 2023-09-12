package seaung.uoscafeteriamenu.crawling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import seaung.uoscafeteriamenu.crawling.crawler.CrawlingMealType;
import seaung.uoscafeteriamenu.crawling.crawler.UosRestaurantCrawlingResponse;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.repository.CrawlingTargetRepository;
import seaung.uoscafeteriamenu.domain.repository.UosRestaurantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CrawlingUosRestaurantService extends CrawlingService {

    private final UosRestaurantRepository uosRestaurantRepository;

    @Autowired
    public CrawlingUosRestaurantService(CrawlingTargetRepository uosRestaurantsCrawlingInfoRepository, UosRestaurantRepository uosRestaurantRepository) {
        super(uosRestaurantsCrawlingInfoRepository);
        this.uosRestaurantRepository = uosRestaurantRepository;
    }

    /**
     * <p>삽입 해야할 데이터가 더 많다면 JdbcTemplate를 사용하여 bulk insert를 고려해보자.</p>
     * <br>
     * <p>JdbcTemplate를 고려하는 이유?</p>
     * <p>JPA의 @Id @GeneratedValue(strategy = GenerationType.IDENTITY)에서는 bulk insert를 지원하지 않음</p>
     * <br>
     * @Author 장세웅
     * @Since 2023/08/06
     */
    @Transactional
    public void saveAllCrawlingData(List<List<UosRestaurantCrawlingResponse>> responsesList) {

        List<UosRestaurant> uosRestaurants = new ArrayList<>();

        for(List<UosRestaurantCrawlingResponse> responses: responsesList) {

            for (UosRestaurantCrawlingResponse response : responses) {

                String crawlingDate = response.getRestaurantDate();
                String restaurantName = response.getRestaurantName();

                UosRestaurant.UosRestaurantBuilder builder = UosRestaurant.builder();
                builder.crawlingDate(crawlingDate);
                builder.restaurantName(CrawlingUtils.toUosRestaurantName(restaurantName));
                builder.view(0);
                builder.likeCount(0);

                // 조식, 중식, 석식에 매칭되는 메뉴 추출
                for (Map.Entry<CrawlingMealType, String> menu : response.getMenu().entrySet()) {

                    MealType mealType = MealType.fromName(menu.getKey().name());

                    // 크롤링한 데이터가 있으면 탈출
                    if(isSameDateInDataBase(crawlingDate, CrawlingUtils.toUosRestaurantName(restaurantName), mealType)) break;

                    // 메뉴 전처리
                    pretreatmentMenu(menu, builder);
                    builder.mealType(mealType);
                    UosRestaurant uosRestaurant = builder.build();

                    // 학교에서 메뉴를 제공한 경우에만 저장
                    addUosRestaurant(uosRestaurant, uosRestaurants);
                }
            }
        }

        uosRestaurantRepository.saveAll(uosRestaurants);
    }

    // 학교에서 메뉴를 제공한 경우
    private static void addUosRestaurant(UosRestaurant uosRestaurant, List<UosRestaurant> uosRestaurants) {
        if(!uosRestaurant.getMenuDesc().equals(CrawlingUtils.NOT_PROVIDED_MENU)) {
            uosRestaurants.add(uosRestaurant);
        }
    }

    private boolean isSameDateInDataBase(String crawlingDate, UosRestaurantName restaurantName, MealType mealType) {
        boolean queryResult = uosRestaurantRepository
                .findByCrawlingDateAndRestaurantNameAndMealType(crawlingDate, restaurantName, mealType)
                .isPresent();

        if(queryResult) return true;

        return false;
    }

    private void pretreatmentMenu(Map.Entry<CrawlingMealType, String> menu, UosRestaurant.UosRestaurantBuilder builder) {
        if(CrawlingUtils.hasMenu(menu.getValue())) {
            builder.menuDesc(menu.getValue());
        } else {
            builder.menuDesc(CrawlingUtils.NOT_PROVIDED_MENU);
        }
    }
}
