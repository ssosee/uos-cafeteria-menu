package seaung.uoscafeteriamenu.global.config;

import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import seaung.uoscafeteriamenu.global.ratelimter.BucketResolver;
import seaung.uoscafeteriamenu.web.interceptor.ApiInterceptor;
import seaung.uoscafeteriamenu.domain.cache.service.CacheBotApikeyService;
import seaung.uoscafeteriamenu.web.interceptor.RateLimitInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CacheBotApikeyService botApikeyService;
    private final BucketResolver bucketResolver;
    private String patterns = "/**/api/v1/**";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiInterceptor(botApikeyService))
                .order(1)
                .addPathPatterns(patterns);

        registry.addInterceptor(new RateLimitInterceptor(bucketResolver))
                .order(2)
                .addPathPatterns(patterns);
    }
}
