package com.example.oldfashion;

import am.ik.yavi.core.ConstraintViolation;
import com.example.web.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OldFashionPaymentController {
    private static final Logger LOG = LoggerFactory.getLogger(OldFashionPaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/api/payments")
    public ResponseEntity<ApiResponse<PaymentResponse>> paymentIntent(@RequestBody PaymentRequest request) {
        try {
            paymentService.processPayment("card", request.getAmount());
        } catch (BusinessException e) {
            LOG.error("Payment processing failed: {}", e.getMessage());
            return ResponseEntity.ok()
                    .body(ApiResponse.failure(
                            List.of(ConstraintViolation.builder()
                                    .name("payment")
                                    .message(e.getMessage()))));
        }

        return ResponseEntity.ok()
                .body(ApiResponse.success(new PaymentResponse()));
    }
}
