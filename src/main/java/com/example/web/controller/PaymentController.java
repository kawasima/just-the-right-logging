package com.example.web.controller;

import com.example.web.ApiResponse;
import com.example.web.adapter.FeeApiService;
import com.example.web.domain.Billing;
import com.example.web.domain.rule.DiscountAtMidnight;
import com.example.web.domain.DiscountRule;
import com.example.web.domain.HighwayDrive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private static final Logger LOG = LoggerFactory.getLogger(PaymentController.class);

    private final FeeApiService feeApiService;
    private final List<DiscountRule> rules;

    public PaymentController(FeeApiService feeApiService, List<DiscountRule> rules) {
        this.feeApiService = feeApiService;
        this.rules = rules;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> payment(@RequestBody Map<String, Object> params) {
        DiscountRule discountAtMidnight = new DiscountAtMidnight();
        List<DiscountRule> rules = List.of(discountAtMidnight);

        return HighwayDrive.mapValidator.validate(params)
                .fold(errors -> ResponseEntity.badRequest().body(ApiResponse.failure(errors)),
                        drive ->{
                            long discount = rules.stream()
                                    .filter(rule -> rule.isApplicable(drive))
                                    .map(rule -> rule.discountPercentage(drive))
                                    .max(Long::compare)
                                    .orElse(0L);
                            BigDecimal amount = feeApiService.calculateFee(drive.getVehicleFamily(),
                                    drive.getEntered().getInterchangeCode(),
                                    drive.getExited().getInterchangeCode());
                            Billing billing = Billing.validator.validated(drive.getDriver(),
                                    amount,
                                    discount);
                            return ResponseEntity.ok(ApiResponse.success(billing));
                        });
    }
}
