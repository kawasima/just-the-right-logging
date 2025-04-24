package com.example.web.domain;

import one.cafebabe.businesscalendar4j.BusinessCalendar;
import one.cafebabe.businesscalendar4j.BusinessCalendarBuilder;

import java.time.LocalDate;

public class HolidayUtils {
    private static final BusinessCalendar businessCalendar;
    static {
        businessCalendar = new BusinessCalendarBuilder().build();
    }


    public static boolean isHoliday(LocalDate from) {
        return businessCalendar.isHoliday(from);
    }
}
