package seaung.uoscafeteriamenu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import seaung.uoscafeteriamenu.domain.TestInitData;
import seaung.uoscafeteriamenu.domain.repository.CrawlingTargetRepository;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class UosCafeteriaMenuApplication {

    public static void main(String[] args) {
        SpringApplication.run(UosCafeteriaMenuApplication.class, args);
    }

    @Bean
    @Profile("local")
    public TestInitData testInitData(CrawlingTargetRepository crawlingTargetRepository) {
        return new TestInitData(crawlingTargetRepository);
    }
}
