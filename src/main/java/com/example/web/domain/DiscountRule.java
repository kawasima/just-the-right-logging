package com.example.web.domain;

public interface DiscountRule {
    boolean isApplicable(HighwayDrive drive);
    long discountPercentage(HighwayDrive drive);
}
