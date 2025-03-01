package com.app.us_twogether.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final SpaceAccessInterceptor spaceAccessInterceptor;

    @Value("${app.api.base-url}")
    private String apiBaseUrl;

    public WebConfig(SpaceAccessInterceptor spaceAccessInterceptor) {
        this.spaceAccessInterceptor = spaceAccessInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(spaceAccessInterceptor)
                .addPathPatterns(apiBaseUrl + "/spaces/{spaceId}/**")
                .excludePathPatterns(apiBaseUrl + "/spaces/join/**");
    }
}
