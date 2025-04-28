package com.example.web.config;

import com.example.web.interceptor.MdcSetupInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final MdcSetupInterceptor mdcSetupInterceptor;
    public WebConfig(MdcSetupInterceptor mdcSetupInterceptor) {
        this.mdcSetupInterceptor = mdcSetupInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mdcSetupInterceptor)
                .addPathPatterns("/api/**");
    }
}
