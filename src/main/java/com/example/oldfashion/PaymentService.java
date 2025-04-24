package com.example.oldfashion;

import org.springframework.transaction.annotation.Transactional;

public class PaymentService {
    @Transactional
    public boolean processPayment(String paymentMethod, double amount) {
        // Simulate payment processing logic
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            throw new BusinessException("Payment method is required");
        }
        if (amount <= 0) {
            throw new BusinessException("Amount must be greater than zero");
        }
        return true;
    }
}
