package com.example.web.domain;

import am.ik.yavi.arguments.Arguments1Validator;
import am.ik.yavi.arguments.IntegerValidator;
import am.ik.yavi.builder.IntegerValidatorBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@ToString
@EqualsAndHashCode
public class Driver {
    @Getter
    private final int countPerMonth;

    private Driver(int countPerMonth) {
        this.countPerMonth = countPerMonth;
    }

    static final IntegerValidator<Integer> countPerMonthValidator = IntegerValidatorBuilder.of("countPerMonth",
            c -> c.greaterThanOrEqual(0))
            .build();
    public static final Arguments1Validator<Map<String, Object>, Driver> mapValidator = countPerMonthValidator
            .<Map<String, Object>>compose(m -> (int) m.get("countPerMonth"))
            .andThen(Driver::new);

}
