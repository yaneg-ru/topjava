package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal save(Meal meal, int userId) {
        log.info("save {} with userId={}", meal, userId);
        if (meal==null || meal.getUserId() != userId) {
            throw new NotFoundException(String.format("create %s with userId=%d", meal, userId));
        }
        return repository.save(meal);
    }

    public void delete(int id, int userId) {
        log.info("delete meal with id={} with userId={}",id , userId);
        if (!repository.delete(id, userId)) {
            throw new NotFoundException(String.format("delete meal with id %d with userId=%d", id, userId));
        }
    }

    public Meal get(int id, int userId) {
        log.info("get meal with id={} with userId={}", id, userId);
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAll with userId={} and filters...", userId);
        return repository.getAll(userId, startDate, endDate, startTime, endTime);
    }
}