package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
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
            saveUserMeal((meal.getId() % 2 == 0 ? 1 : 2), meal);
        }
    }

    private void saveUserMeal(int userId, Meal meal) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals == null) userMeals = new HashMap<>();
        userMeals.put(meal.getId(), meal);
        repository.put(userId, userMeals);
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
        Map<Integer, Meal> userMeals = repository.get(userId);
        Meal mealFromRepository =
                (userMeals != null
                        ? userMeals.get(meal.getId())
                        : null);
        if (mealFromRepository == null) return null;
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
        Map<Integer, Meal> userMeals = repository.get(userId);
        Meal mealToDelete =
                (userMeals != null
                        ? userMeals.get(mealId)
                        : null);
        if (mealToDelete == null) return false;
        return (userMeals.remove(mealId) != null);
    }

    // null if not found
    @Override
    public Meal get(int userId, int mealId) {
        log.info("get meal with mealId={} with userId={}", mealId, userId);
        Map<Integer, Meal> userMeals = repository.get(userId);
        return (userMeals != null
                ? userMeals.get(mealId)
                : null);
    }

    @Override
    public List<MealTo> getAllByUserByFilters(Collection<MealTo> userMealTo, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAllByUserByFilters");
        Comparator<MealTo> comparator = (o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime());

        return userMealTo
                .stream()
                .filter(mealTo -> isBetweenDateTime(mealTo.getDateTime().toLocalDate(), startDate, endDate, mealTo.getDateTime().toLocalTime(), startTime, endTime))
                .sorted(comparator.reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllByUser(int userId) {
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

