package seaung.uoscafeteriamenu.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import seaung.uoscafeteriamenu.domain.repository.ApikeyRepository;
import seaung.uoscafeteriamenu.web.interceptor.ApiInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ApikeyRepository apikeyRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiInterceptor(apikeyRepository))
                .order(1)
                .addPathPatterns("/**/api/v1/**");
    }
}
