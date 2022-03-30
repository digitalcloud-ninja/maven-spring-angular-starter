package ninja.digitalcloud.application.backend.configurations;

import ninja.digitalcloud.application.backend.configurations.filters.HTTPRequestResponseLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class LoggingConfiguration {
    @Bean
    public FilterRegistrationBean<Filter> registerHttpRequestResponseLoggingFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(httpRequestResponseLoggingFilter());
        registration.setUrlPatterns(List.of("/*"));
        registration.setName("httpRequestResponseLoggingFilter");
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public Filter httpRequestResponseLoggingFilter() {
        return new HTTPRequestResponseLoggingFilter();
    }
}
