package seaung.uoscafeteriamenu.global.config;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import seaung.uoscafeteriamenu.global.provider.TimeProvider;

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
