package com.example.web.domain;

import am.ik.yavi.arguments.StringValidator;
import am.ik.yavi.builder.StringValidatorBuilder;

import java.util.Set;

public enum VehicleFamily {
    STANDARD,
    MINI,
    MOTORCYCLE,
    OTHER;

    public static final StringValidator<VehicleFamily> validator = StringValidatorBuilder.of("vehicleFamily",
                    c -> c.notBlank().oneOf(Set.of("STANDARD", "MINI", "MOTORCYCLE", "OTHER")))
            .build(VehicleFamily::valueOf);
}
