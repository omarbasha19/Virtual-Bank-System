package com.VirtualBankingSystem.Common.Config;

import com.VirtualBankingSystem.Common.Logging.RequestResponseLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebMvcConfig {

    @Bean
    public FilterRegistrationBean<RequestResponseLoggingFilter> loggingFilter(RequestResponseLoggingFilter loggingFilter) {
        FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(loggingFilter);
        registrationBean.addUrlPatterns("/*"); // Apply to all paths
        registrationBean.setOrder(1); // Set a high priority
        return registrationBean;
    }
}
