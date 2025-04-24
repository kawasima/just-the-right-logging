package com.example.web.domain.rule;

import com.example.web.domain.DiscountRule;
import com.example.web.domain.HighwayDrive;
import com.example.web.domain.RulePeriod;

public class DiscountAtMidnight implements DiscountRule {
    private final RulePeriod midnight = new RulePeriod(0, 4);
    @Override
    public boolean isApplicable(HighwayDrive drive) {
        return midnight.isIn(drive);
    }

    @Override
    public long discountPercentage(HighwayDrive drive) {
        return 30;
    }
}
