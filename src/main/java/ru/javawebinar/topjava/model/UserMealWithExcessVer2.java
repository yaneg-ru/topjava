package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class UserMealWithExcessVer2 {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    public static Map<LocalDate,Boolean> excessPerDays = new HashMap<>();

    public UserMealWithExcessVer2(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    @Override
    public String toString() {
        if (excessPerDays.get(dateTime.toLocalDate())!=null) {
            return "UserMealWithExcess{" +
                    "dateTime=" + dateTime +
                    ", description='" + description + '\'' +
                    ", calories=" + calories +
                    ", excess=" + excessPerDays.get(dateTime.toLocalDate()) +
                    '}';
        }
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess= ?" +
                '}';

    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

}
