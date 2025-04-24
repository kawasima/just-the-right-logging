package com.example.web.domain;

import am.ik.yavi.arguments.*;
import am.ik.yavi.builder.BigDecimalValidatorBuilder;
import am.ik.yavi.builder.LongValidatorBuilder;
import am.ik.yavi.builder.ObjectValidatorBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ToString
@EqualsAndHashCode
public class Billing {
    @JsonProperty
    @Getter
    private final Driver driver;
    private final BigDecimal amount;

    private final long discountPercentage;

    private Billing(Driver driver, BigDecimal amount, long discountPercentage) {
        this.driver = driver;
        this.amount = amount;
        this.discountPercentage = discountPercentage;
    }

    @JsonProperty
    public BigDecimal getBillingAmount() {
        return amount.subtract(amount.multiply(BigDecimal.valueOf(discountPercentage)).divide(BigDecimal.valueOf(100), RoundingMode.CEILING));
    }
    static ObjectValidator<Driver, Driver> driverValidator = ObjectValidatorBuilder.<Driver>of("driver",
            c -> c.notNull())
            .build();
    static BigDecimalValidator<BigDecimal> amountValidator = BigDecimalValidatorBuilder.of("amount", c -> c.greaterThanOrEqual(BigDecimal.ONE))
            .build();
    static LongValidator<Long> discountPercentageValidator = LongValidatorBuilder.of("discountPercentage",
            c -> c.greaterThanOrEqual(0L).lessThanOrEqual(100L))
            .build();
    public static Arguments3Validator<Driver, BigDecimal, Long, Billing> validator = ArgumentsValidators.split(
            driverValidator,
            amountValidator,
            discountPercentageValidator
    ).apply(Billing::new);
}
