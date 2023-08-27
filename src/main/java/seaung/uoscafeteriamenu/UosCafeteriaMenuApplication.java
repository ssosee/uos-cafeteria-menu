package seaung.uoscafeteriamenu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import seaung.uoscafeteriamenu.crawling.crawler.Crawler;
import seaung.uoscafeteriamenu.crawling.service.CrawlingUosRestaurantService;
import seaung.uoscafeteriamenu.domain.repository.CrawlingTargetRepository;
import seaung.uoscafeteriamenu.domain.repository.SkillBlockRepository;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class UosCafeteriaMenuApplication {

    public static void main(String[] args) {
        SpringApplication.run(UosCafeteriaMenuApplication.class, args);
    }

    @Bean
    @Profile("prod")
    public ProdInitData ProdInitData(CrawlingTargetRepository crawlingTargetRepository, Crawler crawler, CrawlingUosRestaurantService crawlingStudentHallService, SkillBlockRepository skillBlockRepository) {
        return new ProdInitData(crawlingTargetRepository, crawler, crawlingStudentHallService, skillBlockRepository);
    }

    @Bean
    @Profile("local")
    public LocalInitData LocalInitData(CrawlingTargetRepository crawlingTargetRepository, Crawler crawler, CrawlingUosRestaurantService crawlingStudentHallService, SkillBlockRepository skillBlockRepository) {
        return new LocalInitData(crawlingTargetRepository, crawler, crawlingStudentHallService, skillBlockRepository);
    }
}
