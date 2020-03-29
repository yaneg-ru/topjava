package ru.javawebinar.topjava.util.dateformatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.Locale;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

public final class LocalTimeFormatter implements Formatter<LocalTime> {

    public LocalTimeFormatter() {
    }

    public String print(LocalTime time, Locale locale) {
        if (time == null) {
            return "";
        }
        return time.format(ISO_LOCAL_TIME);
    }

    public LocalTime parse(String formatted, Locale locale) throws ParseException {
        if (formatted.length() == 0) {
            return LocalTime.MAX;
        }
        return LocalTime.parse(formatted, ISO_LOCAL_TIME);
    }

}
