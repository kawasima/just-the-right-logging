package com.example.web.controller;

import com.example.logging.AuditLogger;
import com.example.web.ApiResponse;
import com.example.web.Member;
import com.example.web.adapter.BillingRepository;
import com.example.web.adapter.FeeApiService;
import com.example.web.domain.Billing;
import com.example.web.domain.DiscountRule;
import com.example.web.domain.HighwayDrive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private static final Logger LOG = LoggerFactory.getLogger(PaymentController.class);
    private static final AuditLogger AUDIT_LOG = AuditLogger.getLogger(LOG);


    private final BillingRepository billingRepository;
    private final FeeApiService feeApiService;
    private final List<DiscountRule> rules;

    public PaymentController(BillingRepository billingRepository, FeeApiService feeApiService, List<DiscountRule> rules) {
        this.billingRepository = billingRepository;
        this.feeApiService = feeApiService;
        this.rules = rules;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> payment(@RequestBody Map<String, Object> params) {
        return HighwayDrive.mapValidator.validate(params)
                .fold(errors -> ResponseEntity.badRequest().body(ApiResponse.failure(errors)),
                        drive ->{
                            // 高速道路の入出記録を元に
                            // 料金APIを呼び出して料金を計算する
                            BigDecimal amount = feeApiService.calculateFee(drive.getVehicleFamily(),
                                    drive.getEntered().getInterchangeCode(),
                                    drive.getExited().getInterchangeCode());

                            // 適用される割引率を計算する
                            long discount = rules.stream()
                                    .filter(rule -> rule.isApplicable(drive))
                                    .map(rule -> rule.discountPercentage(drive))
                                    .max(Long::compare)
                                    .orElse(0L);
                            LOG.debug("Amount: {}, discount: {}", amount, discount);
                            // 請求を作る
                            Billing billing = Billing.create(
                                    drive.getDriver(),
                                    amount,
                                    discount);
                            billingRepository.save(billing);
                            return ResponseEntity.ok(ApiResponse.success(billing));
                        });
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<?>> getPayment(@PathVariable("paymentId") String paymentId,
                                                     @AuthenticationPrincipal Member member) {
        List<Billing> billings = billingRepository.findByMemberId(member.getUsername());
        AUDIT_LOG.log(Map.of("billingId", billings.stream().map(Billing::getId).toList()));
        return ResponseEntity.ok(ApiResponse.success(billings));
    }
}
