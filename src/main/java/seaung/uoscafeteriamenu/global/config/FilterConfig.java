package seaung.uoscafeteriamenu.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import seaung.uoscafeteriamenu.web.filter.ServletCachingWrapperFilter;

import javax.servlet.Filter;

@Configuration
public class FilterConfig {

    private String patterns = "/api/v1/*";

    @Bean
    public FilterRegistrationBean<Filter> servletCachingWrapperFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new ServletCachingWrapperFilter());
        filterRegistrationBean.setOrder(0);
        filterRegistrationBean.addUrlPatterns(patterns);

        return filterRegistrationBean;
    }
}
