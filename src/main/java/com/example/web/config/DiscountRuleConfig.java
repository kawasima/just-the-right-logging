package com.example.web.config;

import com.example.web.domain.DiscountRule;
import com.example.web.domain.rule.DiscountAtMidnight;
import com.example.web.domain.rule.DiscountInMorningOrEvening;
import com.example.web.domain.rule.DiscountOnHoliday;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DiscountRuleConfig {
    @Bean
    public List<DiscountRule> discountRules() {
        return List.of(
                new DiscountAtMidnight(),
                new DiscountInMorningOrEvening(),
                new DiscountOnHoliday()
        );
    }

}
