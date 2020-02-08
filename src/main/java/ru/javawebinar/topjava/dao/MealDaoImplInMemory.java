package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class MealDaoImplInMemory implements MealDao {

    private final static Map<Long, Meal> meals = MealsUtil.generateMap();
    private static final AtomicLong lastId = new AtomicLong();

    static {
        lastId.set(meals.size());
    }

    private static Long getNextId() {
        return lastId.incrementAndGet();
    }

    @Override
    public synchronized void addMeal(LocalDateTime localDateTime, String description, int calories) {
        Long id = getNextId();
        meals.put(id, new Meal(id,localDateTime,description,calories));
    }

    @Override
    public synchronized void deleteMeal(Long id) {
        meals.remove(id);
    }

    @Override
    public synchronized void updateMeal(Meal meal) {
        meals.put(meal.getId(), meal);
    }

    @Override
    public synchronized List<Meal> getAllMeals() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public synchronized Meal getMealById(Long id) {
        return meals.get(id);
    }
}
