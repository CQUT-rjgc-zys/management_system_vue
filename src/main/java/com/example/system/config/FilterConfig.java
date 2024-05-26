package com.example.system.config;

import com.example.system.filter.AuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilterRegistration(AuthenticationFilter authenticationFilter) {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(authenticationFilter);
        registrationBean.addUrlPatterns("/*"); // 默认过滤所有路径
        registrationBean.setOrder(1); // 设置过滤器的优先级

        // 设置不需要过滤的路径
        registrationBean.addInitParameter("excludedUris",
                "/adminLogin,/employeeLogin,/importEmployee");
        return registrationBean;
    }

}
