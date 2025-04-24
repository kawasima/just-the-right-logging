package com.example.web.controller;

import com.example.web.ApiResponse;
import com.example.web.adapter.FeeApiService;
import com.example.web.config.DiscountRuleConfig;
import com.example.web.config.RestTemplateConfig;
import com.example.web.domain.DiscountRule;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@SpringJUnitConfig(classes = {
        PaymentControllerTest.TestConfig.class,
        DiscountRuleConfig.class,
        RestTemplateConfig.class
})
class PaymentControllerTest {
    @Configuration
    static class TestConfig {
        @Bean
        public PaymentController paymentController(FeeApiService feeApiService,
                                                   List<DiscountRule> rules) {
            return new PaymentController(feeApiService, rules);
        }
        @Bean
        public FeeApiService feeApiService(RestTemplate restTemplate) {
            return new FeeApiService(restTemplate);
        }
    }

    @Test
    void test(ApplicationContext ctx) {
        PaymentController paymentController = ctx.getBean(PaymentController.class);
        ResponseEntity<ApiResponse<?>> response = paymentController.payment(Map.of(
                "driver", Map.of("countPerMonth", 5),
                "entered", Map.of(
                        "interchangeCode", "A12345678",
                        "passedAt", "2023-10-01T01:00:00"
                ),
                "exited", Map.of(
                        "interchangeCode", "A12345679",
                        "passedAt", "2023-10-01T03:00:00"
                ),
                "vehicleFamily", "STANDARD",
                "routeType", "URBAN"
        ));
        System.out.println(response.getBody());
    }
}