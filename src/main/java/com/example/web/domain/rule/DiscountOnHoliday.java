package com.example.web.domain.rule;

import com.example.web.domain.*;

import java.time.LocalDate;
import java.util.EnumSet;

import static com.example.web.domain.RouteType.RURAL;
import static com.example.web.domain.VehicleFamily.*;

public class DiscountOnHoliday implements DiscountRule {
    private static final EnumSet<VehicleFamily> targetVehicleFamilies =
            EnumSet.of(STANDARD, MOTORCYCLE, MINI);

    @Override
    public boolean isApplicable(HighwayDrive drive) {
        LocalDate enteredAt = drive.getEntered().getPassedAt().toLocalDate();
        LocalDate exitedAt = drive.getExited().getPassedAt().toLocalDate();

        return (HolidayUtils.isHoliday(enteredAt))
                || HolidayUtils.isHoliday(exitedAt)
                && targetVehicleFamilies.contains(drive.getVehicleFamily())
                && drive.getRouteType() == RURAL;
    }

    @Override
    public long discountPercentage(HighwayDrive drive) {
        return 30;
    }
}
