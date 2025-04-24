package com.example.web.config;

import com.example.web.interceptor.RemoteCallLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    private static final Logger LOG = LoggerFactory.getLogger("restClient");
    @Bean
    public RestTemplate restTemplate() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return builder
                .connectTimeout(Duration.ofMillis(500L))
                .readTimeout(Duration.ofMillis(5000L))
                .requestFactory(() -> new BufferingClientHttpRequestFactory(
                        new SimpleClientHttpRequestFactory()
                ))
                .additionalInterceptors(
                        new RemoteCallLoggingInterceptor()
                )
                .build();
    }
}
