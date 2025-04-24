package com.example.web.domain;

import am.ik.yavi.core.Validated;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


class HighwayDriveTest {
    @Test
    void validDriveData() {
        Validated<HighwayDrive> validated = HighwayDrive.mapValidator.validate(
                Map.of(
                        "driver", Map.of("countPerMonth", 5),
                        "entered", Map.of(
                                "interchangeCode", "A12345678",
                                "passedAt", LocalDateTime.parse("2023-10-01T10:00:00")
                        ),
                        "exited", Map.of(
                                "interchangeCode", "A12345679",
                                "passedAt", LocalDateTime.parse("2023-10-01T11:00:00")
                        ),
                        "vehicleFamily", "STANDARD",
                        "routeType", "URBAN"
                )
        );
        assertThat(validated.isValid()).isTrue();
    }

    @Test
    void minusCountPerMonthIsInvalid() {
        Validated<HighwayDrive> validated = HighwayDrive.mapValidator.validate(
                Map.of(
                        "driver", Map.of("countPerMonth", -1),
                        "entered", Map.of(
                                "interchangeCode", "A12345678",
                                "passedAt", LocalDateTime.parse("2023-10-01T10:00:00")
                        ),
                        "exited", Map.of(
                                "interchangeCode", "A12345679",
                                "passedAt", LocalDateTime.parse("2023-10-01T11:00:00")
                        ),
                        "vehicleFamily", "STANDARD",
                        "routeType", "URBAN"
                )
        );
        assertThat(validated.isValid()).isFalse();

    }
}