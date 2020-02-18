package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends LocalTime> boolean isBetweenInclusive(T lt, T sTime, T eTime) {
        return lt.compareTo(sTime) >= 0 && lt.compareTo(eTime) <= 0;
    }

    public static <D extends LocalDate, T extends LocalTime> boolean isBetweenDateTime(D ld, D sDate, D eDate, T lt, T sTime, T eTime) {
        return isBetweenInclusive(lt, sTime, eTime) && (ld.compareTo(sDate) >= 0 && ld.compareTo(eDate) <= 0);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

}