package com.example.web.domain.rule;

import com.example.web.domain.DiscountRule;
import com.example.web.domain.HighwayDrive;
import com.example.web.domain.RouteType;
import com.example.web.domain.RulePeriod;

public class DiscountInMorningOrEvening implements DiscountRule {
    private final RulePeriod morning = new RulePeriod(6, 9);
    private final RulePeriod evening = new RulePeriod(17, 20);

    @Override
    public boolean isApplicable(HighwayDrive drive) {
        return ((!morning.isHoliday(drive) && morning.isIn(drive))
                || (!evening.isHoliday(drive) && evening.isIn(drive)))
                && drive.getRouteType() == RouteType.RURAL;
    }

    @Override
    public long discountPercentage(HighwayDrive drive) {
        int count = drive.getDriver().getCountPerMonth();
        if (count >= 10) {
            return 50;
        } else if (count >= 5){
            return 30;
        } else {
            return 0;
        }
    }}
