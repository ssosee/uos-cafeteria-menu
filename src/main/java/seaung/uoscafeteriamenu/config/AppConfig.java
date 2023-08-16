package seaung.uoscafeteriamenu.config;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import seaung.uoscafeteriamenu.provider.TimeProvider;

@Configuration
public class AppConfig {
    @Bean
    public Connection connection() {
        return new HttpConnection();
    }

    @Bean
    public TimeProvider timeProvider() {
        return new TimeProvider();
    }
}
