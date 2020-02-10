package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealDaoImplInMemory implements MealDao {

    private final Map<Long, Meal> meals;
    private final AtomicLong lastId = new AtomicLong();

    public MealDaoImplInMemory() {
        meals = new ConcurrentHashMap( MealsUtil.generateMap());
        lastId.set(meals.size());
    }

    private Long getNextId() {
        return lastId.incrementAndGet();
    }

    @Override
    public void add(Meal meal) {
        Long id = getNextId();
        meals.put(id, new Meal(id, meal.getDateTime(),meal.getDescription(),meal.getCalories()));
    }

    @Override
    public void delete(Long id) {
        meals.remove(id);
    }

    @Override
    public void update(Meal meal) {
        meals.put(meal.getId(), meal);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal getById(Long id) {
        return meals.get(id);
    }
}
