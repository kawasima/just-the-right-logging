package com.example.web.domain;

import am.ik.yavi.arguments.EnumValidator;
import am.ik.yavi.arguments.StringValidator;
import am.ik.yavi.builder.StringValidatorBuilder;
import am.ik.yavi.core.Validated;

import java.util.Set;

public enum RouteType {
    RURAL,
    URBAN;

    public static final StringValidator<RouteType> validator = StringValidatorBuilder.of("routeType",
            c -> c.notBlank().oneOf(Set.of("RURAL", "URBAN")))
            .build(RouteType::valueOf);


}
