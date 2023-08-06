package seaung.uoscafeteriamenu.crawling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.crawling.crawler.CrawlingMealType;
import seaung.uoscafeteriamenu.crawling.crawler.UosRestaurantCrawlingResponse;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.StudentHall;
import seaung.uoscafeteriamenu.domain.repository.StudentHallRepository;
import seaung.uoscafeteriamenu.domain.repository.CrawlingTargetRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CrawlingStudentHallService extends CrawlingService {

    private final StudentHallRepository studentHallRepository;

    @Autowired
    public CrawlingStudentHallService(CrawlingTargetRepository uosRestaurantsCrawlingInfoRepository, StudentHallRepository studentHallRepository) {
        super(uosRestaurantsCrawlingInfoRepository);
        this.studentHallRepository = studentHallRepository;
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

        List<StudentHall> studentHalls = new ArrayList<>();

        for(UosRestaurantCrawlingResponse response : responses) {
            StudentHall.StudentHallBuilder builder = StudentHall.builder();
            builder.crawlingDate(response.getRestaurantDate());

            // 조식, 중식, 석식에 매칭되는 메뉴 추출
            for(Map.Entry<CrawlingMealType, String> menu : response.getMenu().entrySet()) {
                builder.mealType(MealType.valueOf(menu.getKey().name()));
                builder.menuDesc(menu.getValue());

                StudentHall studentHall = builder.build();
                studentHalls.add(studentHall);
            }
        }

        studentHallRepository.saveAll(studentHalls);
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
        List<StudentHall> studentHalls = responses.stream()
                .map(response -> {
                    StudentHall.StudentHallBuilder builder = StudentHall.builder()
                            .crawlingDate(response.getRestaurantDate());

                    response.getMenu().forEach((crawlingMealType, menuDesc) -> {
                        MealType mealType = MealType.valueOf(crawlingMealType.name());
                        builder.mealType(mealType)
                                .menuDesc(menuDesc);
                    });

                    return builder.build();
                })
                .collect(Collectors.toList());

        studentHallRepository.saveAll(studentHalls);
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
