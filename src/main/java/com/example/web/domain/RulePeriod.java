package com.example.web.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RulePeriod {
    private final LocalTime startTime;
    private final LocalTime endTime;

    public RulePeriod(int startHour, int endHour) {
        startTime = LocalTime.of(startHour, 0);
        endTime   = LocalTime.of(endHour, 0);
    }

    public boolean isIn(HighwayDrive drive) {
        PassEvent entered = drive.getEntered();
        PassEvent exited = drive.getExited();

        int offset = entered.getPassedAt().toLocalTime().isBefore(endTime) ? 0 : 1;
        LocalDateTime start = LocalDateTime.of(
                LocalDate.from(entered.getPassedAt().plusDays(offset)),
                startTime);
        LocalDateTime end = LocalDateTime.of(
                LocalDate.from(exited.getPassedAt().plusDays(offset)),
                endTime);
        return entered.getPassedAt().isBefore(end) && exited.getPassedAt().isAfter(start);
    }

    public boolean isHoliday(HighwayDrive drive) {
        PassEvent entered = drive.getEntered();
        int offset = entered.getPassedAt().toLocalTime().isBefore(endTime) ? 0 : 1;
        return HolidayUtils.isHoliday(LocalDate.from(entered.getPassedAt().plusDays(offset)));
    }
}
