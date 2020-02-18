package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface MealRepository {
    // null if not found, when updated
    Meal save(Meal meal, int userId);

    // false if not found
    boolean delete(int userId, int mealId);

    // null if not found
    Meal get(int userId, int mealId);

    List<MealTo> getAllByUserByFilters(Collection<MealTo> userMealTo, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime);
    List<Meal> getAllByUser(int userId);
}
