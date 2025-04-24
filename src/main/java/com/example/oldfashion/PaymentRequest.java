package com.example.oldfashion;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentRequest {
    @NotNull
    @Min(1)
    @Max(100_000_000)
    private int amount;

    @Min(0)
    private int countPerMonth;

    private LocalDateTime enteredAt;
    private LocalDateTime exitedAt;

    private String vehicleType;

    private String routeType;
}

