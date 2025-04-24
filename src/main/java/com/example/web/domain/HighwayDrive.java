package com.example.web.domain;

import am.ik.yavi.arguments.*;
import am.ik.yavi.core.ConstraintViolation;
import am.ik.yavi.core.Validated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Range;

import java.util.Map;

@ToString
@EqualsAndHashCode
public class HighwayDrive {
    @Getter
    private final Driver driver;

    private final Range<PassEvent> passEvents;
    @Getter
    private final VehicleFamily vehicleFamily;
    @Getter
    private final RouteType routeType;

    private HighwayDrive(Driver driver, Range<PassEvent> passEvents, VehicleFamily vehicleFamily, RouteType routeType) {
        this.driver = driver;
        this.passEvents = passEvents;
        this.vehicleFamily = vehicleFamily;
        this.routeType = routeType;
    }

    public static Arguments2Validator<PassEvent, PassEvent, Range<PassEvent>> passEventsValidator = (entered, exited, locale, context) -> {
        if (entered.getPassedAt().isAfter(exited.getPassedAt())) {
            return Validated.failureWith(ConstraintViolation.builder()
                    .name("enteredAt")
                    .messageKey("enteredAt")
                    .defaultMessageFormat("Entered date time {0} must be before exited date time {1}")
                    .args(entered.getPassedAt(), exited.getPassedAt())
                    .build());
        }
        return Validated.successWith(Range.closed(entered, exited));
    };

    public static Arguments1Validator<Map<String, Object>, HighwayDrive> mapValidator = ArgumentsValidators
            .combine(
                    Driver.mapValidator.<Map<String, Object>>compose(m -> (Map<String, Object>) m.get("driver")),
                    ArgumentsValidators.combine(PassEvent.mapValidator.<Map<String, Object>>compose(m -> (Map<String, Object>) m.get("entered")),
                                    PassEvent.mapValidator.<Map<String, Object>>compose(m -> (Map<String, Object>) m.get("exited")))
                            .apply(passEventsValidator::validated),
                    VehicleFamily.validator.<Map<String, Object>>compose(m -> (String) m.get("vehicleFamily")),
                    RouteType.validator.<Map<String, Object>>compose(m -> (String) m.get("routeType"))
            ).apply(HighwayDrive::new);

    public PassEvent getEntered() {
        return passEvents.getLowerBound().getValue().orElseThrow();
    }

    public PassEvent getExited() {
        return passEvents.getUpperBound().getValue().orElseThrow();
    }
}
