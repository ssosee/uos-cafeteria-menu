package seaung.uoscafeteriamenu.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.entity.CrawlingTarget;
import seaung.uoscafeteriamenu.domain.repository.CrawlingTargetRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TestInitData {
    private final CrawlingTargetRepository crawlingTargetRepository;

    private final String studentHallCssQuery = "div.listType02#week table tbody tr";

    private final String studentHallUrl = "https://english.uos.ac.kr/food/placeList.do";
    private final String mainBuildingUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=010&menuid=2000005006002000000";
    private final String museumOfNaturalScienceUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=040&menuid=2000005006002000000";
    private final String westernRestaurantUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=030&menuid=2000005006002000000";
    private final String welfareTeamTel = "02-6490-5855";
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        CrawlingTarget studentHall = createTarget(UosRestaurantName.STUDENT_HALL, studentHallUrl, welfareTeamTel, studentHallCssQuery);
        crawlingTargetRepository.saveAll(List.of(studentHall));
    }

    private CrawlingTarget createTarget(UosRestaurantName uosRestaurantName, String url, String welfareTeamTel, String cssQuery) {
        return CrawlingTarget.builder()
                .restaurantsName(uosRestaurantName)
                .url(url)
                .welfareTeamTel(welfareTeamTel)
                .cssQuery(cssQuery)
                .build();
    }
}
