package seaung.uoscafeteriamenu.config;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private String url;
    @Bean
    public Connection connection() {
        return new HttpConnection();
    }
}
