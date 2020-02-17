package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal save(Meal meal, int userId) {
        log.info("save {} with userId={}", meal, userId);
        return repository.save(meal, userId);
    }

    public void delete(int mealId, int userId) {
        log.info("delete meal with id={} with userId={}",mealId , userId);
        checkNotFoundWithId(repository.delete(userId, mealId), mealId);
    }

    public Meal get(int mealId, int userId) {
        log.info("get meal with id={} with userId={}", mealId, userId);
        return checkNotFoundWithId(repository.get(userId, mealId), mealId);
    }

    public List<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAll with userId={} and filters...", userId);
        return repository.getAll(userId, startDate, endDate, startTime, endTime);
    }

    public List<Meal> getAll(int userId) {
        log.info("getAll with userId={}", userId);
        return repository.getAll(userId);
    }

}