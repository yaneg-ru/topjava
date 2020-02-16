package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal extends AbstractBaseEntity {

    private final int userId;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    public Meal(int userId,  LocalDateTime dateTime, String description, int calories) {
        this(userId, null, dateTime, description, calories);
    }

    public Meal(int userId, Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.userId = userId;
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

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public boolean isNew() {
        return id == null;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "userId=" + userId +
                ", id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
