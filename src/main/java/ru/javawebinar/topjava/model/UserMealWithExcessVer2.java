package ru.javawebinar.topjava.model;

import jdk.management.resource.internal.ApproverGroup;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class UserMealWithExcessVer2 {
    private static Map<LocalDate, Integer> excessPerDays = new HashMap<>();
    private static int limitCaloriesPerDay;
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;

    public UserMealWithExcessVer2(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        updateExcessPerDays(dateTime.toLocalDate(), calories);
    }


    private static void updateExcessPerDays(LocalDate localDate, int calories) {
        excessPerDays.merge(localDate, calories, Integer::sum);
    }

    public static void clearExcessPerDays() {
        excessPerDays.clear();
    }

    public static int getLimitCaloriesPerDay() {
        return limitCaloriesPerDay;
    }

    public static void setLimitCaloriesPerDay(int caloriesPerDay) {
        limitCaloriesPerDay = caloriesPerDay;
    }

    public boolean getExcess() {
        if (excessPerDays.get(dateTime.toLocalDate()) != null) {
            return excessPerDays.get(dateTime.toLocalDate()) > UserMealWithExcessVer2.limitCaloriesPerDay;
        }
        return false;
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + getExcess() +
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
