package com.example.web.domain;

import am.ik.yavi.arguments.*;
import am.ik.yavi.builder.BigDecimalValidatorBuilder;
import am.ik.yavi.builder.LongValidatorBuilder;
import am.ik.yavi.builder.ObjectValidatorBuilder;
import am.ik.yavi.builder.StringValidatorBuilder;
import am.ik.yavi.core.Constraint;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@ToString
@EqualsAndHashCode
public class Billing {
    @Getter
    private final UUID id;

    @Getter
    private final Driver driver;
    private final BigDecimal amount;

    private final long discountPercentage;

    private Billing(UUID id, Driver driver, BigDecimal amount, long discountPercentage) {
        this.id = id;
        this.driver = driver;
        this.amount = amount;
        this.discountPercentage = discountPercentage;
    }

    @JsonProperty
    public BigDecimal getBillingAmount() {
        return amount.subtract(amount.multiply(BigDecimal.valueOf(discountPercentage)).divide(BigDecimal.valueOf(100), RoundingMode.CEILING));
    }
    static final ObjectValidator<Driver, Driver> driverValidator = ObjectValidatorBuilder.<Driver>of("driver",
                    Constraint::notNull)
            .build();
    static final BigDecimalValidator<BigDecimal> amountValidator = BigDecimalValidatorBuilder.of("amount", c -> c.greaterThanOrEqual(BigDecimal.ONE))
            .build();
    static final LongValidator<Long> discountPercentageValidator = LongValidatorBuilder.of("discountPercentage",
            c -> c.notNull().greaterThanOrEqual(0L).lessThanOrEqual(100L))
            .build();
    static final StringValidator<UUID> idValidator = StringValidatorBuilder.of("id", c -> c.notNull().uuid())
            .build()
            .andThen(UUID::fromString);

    public static final Arguments4Validator<String, Driver, BigDecimal, Long, Billing> validator = ArgumentsValidators.split(
            idValidator,
            driverValidator,
            amountValidator,
            discountPercentageValidator
    ).apply(Billing::new);

    public static Billing create(Driver driver, BigDecimal amount, long discountPercentage) {
        return validator.validated(UUID.randomUUID().toString(), driver, amount, discountPercentage);
    }
}
