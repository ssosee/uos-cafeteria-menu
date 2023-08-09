package seaung.uoscafeteriamenu.crawling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import seaung.uoscafeteriamenu.crawling.crawler.CrawlingMealType;
import seaung.uoscafeteriamenu.crawling.crawler.UosRestaurantCrawlingResponse;
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
public class CrawlingStudentHallService extends CrawlingService {

    private final UosRestaurantRepository uosRestaurantRepository;

    @Autowired
    public CrawlingStudentHallService(CrawlingTargetRepository uosRestaurantsCrawlingInfoRepository, UosRestaurantRepository uosRestaurantRepository) {
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
    public void saveAllCrawlingData(List<UosRestaurantCrawlingResponse> responses) {

        List<UosRestaurant> studentHalls = new ArrayList<>();

        for(UosRestaurantCrawlingResponse response : responses) {
            UosRestaurant.UosRestaurantBuilder builder = UosRestaurant.builder();
            builder.crawlingDate(response.getRestaurantDate());
            builder.restaurantName(UosRestaurantName.fromKrName(response.getRestaurantName()));

            // 조식, 중식, 석식에 매칭되는 메뉴 추출
            for(Map.Entry<CrawlingMealType, String> menu : response.getMenu().entrySet()) {
                builder.mealType(MealType.valueOf(menu.getKey().name()));

                // 메뉴 검증
                validationMenu(menu, builder);

                UosRestaurant studentHall = builder.build();
                studentHalls.add(studentHall);
            }
        }

        uosRestaurantRepository.saveAll(studentHalls);
    }

    private void validationMenu(Map.Entry<CrawlingMealType, String> menu, UosRestaurant.UosRestaurantBuilder builder) {
        // 메뉴가 공백이거나 null 이면
        if(StringUtils.hasText(menu.getValue())) {
            builder.menuDesc(menu.getValue());
        } else {
            builder.menuDesc("학교에서 메뉴를 제공하지 않았다.. 휴먼.");
        }
    }

    /**
     * <p>코드 가독성이 좋지 못한것 같다.</p>
     * <br>
     * @Author 장세웅
     * @Since 2023/08/06
     */
    @Deprecated
    @Transactional
    public void saveAllCrawlingDataUseLamDa(List<UosRestaurantCrawlingResponse> responses) {
        List<UosRestaurant> studentHalls = responses.stream()
                .map(response -> {
                    UosRestaurant.UosRestaurantBuilder builder = UosRestaurant.builder()
                            .crawlingDate(response.getRestaurantDate());

                    response.getMenu().forEach((crawlingMealType, menuDesc) -> {
                        MealType mealType = MealType.valueOf(crawlingMealType.name());
                        builder.mealType(mealType)
                                .menuDesc(menuDesc);
                    });

                    return builder.build();
                })
                .collect(Collectors.toList());

        uosRestaurantRepository.saveAll(studentHalls);
    }

    private String mapValueToString(Map<CrawlingMealType, String> menu) {
        return menu.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.joining());
    }

    private String mapKeyToString(Map<CrawlingMealType, String> menu) {
        return menu.entrySet().stream()
                .map(e -> e.getKey().name())
                .collect(Collectors.joining());
    }
}
