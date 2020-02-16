package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenDate;

@Repository
public class InMemoryMealRepository implements MealRepository {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        log.info("initialization repository in memory");
        MealsUtil.MEALS.forEach(this::save);
    }

    // null if not found, when updated
    @Override
    public Meal save(Meal meal) {
        log.info("save {}", meal);
        // new
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // update
        // handle case: update, but not present in storage
        if (repository.containsKey(meal.getId())) {
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        } else {
            return null;
        }
    }

    // false if not found
    @Override
    public boolean delete(int id, int userId) {
        log.info("delete meal with id={} with userId={}",id , userId);
        Meal mealToDelete = repository.get(id);
        if (mealToDelete != null && mealToDelete.getUserId() != userId) return false;
        return repository.remove(id) != null;
    }

    // null if not found
    @Override
    public Meal get(int id, int userId) {
        log.info("get meal with id={} with userId={}",id , userId);
        Meal mealToGet = repository.get(id);
        if (mealToGet != null && mealToGet.getUserId() != userId) return null;
        return mealToGet;
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAll with userId={} and filters...", userId);
        return repository
                .values()
                .stream()
                .filter(meal -> meal.getUserId() == userId)
                .filter(meal -> isBetweenDate(meal.getDate(),startDate, endDate, meal.getTime(), startTime, endTime))
                .sorted((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()))
                .collect(Collectors.toList());
    }
}

