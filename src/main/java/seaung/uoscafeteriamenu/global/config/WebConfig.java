package seaung.uoscafeteriamenu.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import seaung.uoscafeteriamenu.web.interceptor.ApiInterceptor;
import seaung.uoscafeteriamenu.domain.cache.service.CacheBotApikeyService;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CacheBotApikeyService botApikeyService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiInterceptor(botApikeyService))
                .order(1)
                .addPathPatterns("/**/api/v1/**");
    }
}
