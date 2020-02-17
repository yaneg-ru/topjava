package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenDateTime;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        log.info("initialization repository in memory");
        MealsUtil.MEALS.forEach(this::create);
    }

    private void create(Meal meal) {
        log.info("create {}", meal);
        // new
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            saveUserMeal(meal.getUserId(), meal);
        }
    }

    private void saveUserMeal(int userId, Meal meal) {
        Map<Integer, Meal> usersMeal = new HashMap<>();
        if (repository.containsKey(userId)) {
            usersMeal = repository.get(userId);
            usersMeal.put(meal.getId(), meal);
        } else {
            usersMeal.put(meal.getId(), meal);
        }
        repository.put(userId, usersMeal);
    }

    private Meal getUserMeal(int userId, int mealId) {
        Map<Integer, Meal> usersMeals = new HashMap<>();
        if (repository.containsKey(userId)) {
            usersMeals = repository.get(userId);
        } else {
            return null;
        }
        return usersMeals.get(mealId);
    }

    private boolean deleteUserMeal(int userId, int mealId) {
        Map<Integer, Meal> usersMeals = new HashMap<>();
        usersMeals = repository.get(userId);
        if (usersMeals == null) return false;
        return (usersMeals.remove(mealId) == null ? false : true);
    }

    // null if not found, when updated
    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        // new
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            saveUserMeal(userId, meal);
            return meal;
        }
        // update
        // handle case: update, but not present in storage
        Meal mealFromRepository = getUserMeal(userId, meal.getId());
        if (mealFromRepository == null || mealFromRepository.getUserId() != userId)
            throw new NotFoundException("mealFromRepository.getUserId()!=userId");
        repository.computeIfPresent(userId, (usrId, usersMeals) -> {
            saveUserMeal(userId, meal);
            return repository.get(userId);
        });
        return meal;
    }

    // false if not found
    @Override
    public boolean delete(int userId, int mealId) {
        log.info("delete meal with id={} with userId={}", mealId, userId);
        Meal mealToDelete = getUserMeal(userId, mealId);
        if (mealToDelete == null) return false;
        return deleteUserMeal(userId, mealId);
    }

    // null if not found
    @Override
    public Meal get(int userId, int mealId) {
        log.info("get meal with mealId={} with userId=", mealId, userId);
        return getUserMeal(userId, mealId);
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAll with userId={} and filters...", userId);
        Comparator<Meal> comparator = (o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime());

        return repository
                .get(userId)
                .values()
                .stream()
                .filter(meal -> isBetweenDateTime(meal.getDate(), startDate, endDate, meal.getTime(), startTime, endTime))
                .sorted(comparator.reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll with userId={} and filters...", userId);
        Comparator<Meal> comparator = (o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime());
        return repository
                .get(userId)
                .values()
                .stream()
                .sorted(comparator.reversed())
                .collect(Collectors.toList());
    }
}

