package ru.javawebinar.topjava.util.dateformatter;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public final class LocalTimeFormatAnnotationFormatterFactory
        implements AnnotationFormatterFactory<TimeFormat> {

    public Set<Class<?>> getFieldTypes() {
        return new HashSet<Class<?>>(asList(new Class<?>[] {String.class}));
    }

    public Printer<?> getPrinter(TimeFormat annotation, Class<?> fieldType) {
        return new LocalTimeFormatter();
    }

    public Parser<?> getParser(TimeFormat annotation, Class<?> fieldType) {
        return new LocalTimeFormatter();
    }
}