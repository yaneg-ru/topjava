package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealDao {

    void addMeal(LocalDateTime localDateTime, String description, int calories);
    void deleteMeal (Long id);
    void updateMeal (Meal meal);
    List<Meal> getAllMeals();
    Meal getMealById(Long id);

}
