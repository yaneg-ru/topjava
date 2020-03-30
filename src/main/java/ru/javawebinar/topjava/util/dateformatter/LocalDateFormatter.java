package ru.javawebinar.topjava.util.dateformatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public final class LocalDateFormatter implements Formatter<LocalDate> {

    public LocalDateFormatter() {
    }

    public String print(LocalDate date, Locale locale) {
        if (date == null) {
            return "";
        }
        return date.format(ISO_LOCAL_DATE);
    }

    public LocalDate parse(String formatted, Locale locale) throws ParseException {
        if (formatted.length() == 0) {
            return null;
        }
        return LocalDate.parse(formatted, ISO_LOCAL_DATE);
    }
}
