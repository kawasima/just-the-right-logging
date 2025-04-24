package com.example.web.domain;

import am.ik.yavi.arguments.Arguments1Validator;
import am.ik.yavi.arguments.ArgumentsValidators;
import am.ik.yavi.arguments.LocalDateTimeValidator;
import am.ik.yavi.arguments.StringValidator;
import am.ik.yavi.builder.LocalDateTimeValidatorBuilder;
import am.ik.yavi.builder.StringValidatorBuilder;
import am.ik.yavi.core.CustomConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Map;

@ToString
@EqualsAndHashCode
public class PassEvent implements Comparable<PassEvent> {
    @Getter
    private final String interchangeCode;
    @Getter
    @NonNull
    private final LocalDateTime passedAt;

    private PassEvent(String interchangeCode, LocalDateTime passedAt) {
        this.interchangeCode = interchangeCode;
        this.passedAt = passedAt;
    }

    static StringValidator<String> interchangeCodeValidator = StringValidatorBuilder.of("interchangeCode",
            c -> c.notNull().lessThanOrEqual(10))
            .build();
    public static StringValidator<LocalDateTime> passedAtValidator = StringValidatorBuilder.of("passedAt",
                    c -> c.pattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}"))
            .build()
            .andThen(LocalDateTime::parse);

    public static Arguments1Validator<Map<String, Object>, PassEvent> mapValidator = ArgumentsValidators
            .combine(
                    interchangeCodeValidator.<Map<String, Object>>compose(m -> (String) m.get("interchangeCode")),
                    passedAtValidator.<Map<String, Object>>compose(m -> (String) m.get("passedAt"))
            ).apply(PassEvent::new);

    @Override
    public int compareTo(PassEvent other) {
        return this.passedAt.compareTo(other.passedAt);
    }
}

