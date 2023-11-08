package seaung.uoscafeteriamenu.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import seaung.uoscafeteriamenu.api.korea.holiday.HolidayConstant;
import seaung.uoscafeteriamenu.api.korea.holiday.response.HolidayItems;
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

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public UriComponentsBuilder UriComponentsBuilder() {
        return UriComponentsBuilder.newInstance();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.coercionConfigFor(HolidayItems.class)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);

        return om;
    }
}
