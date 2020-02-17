package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public void create(Meal meal) {
        checkNew(meal);
        service.save(meal, SecurityUtil.authUserId());
    }

    public void update(Meal meal) {
        service.save(meal, SecurityUtil.authUserId());
    }


    public void delete(int mealId){
        log.info("delete {} with userId={}", mealId, SecurityUtil.authUserId());
        service.delete(mealId, SecurityUtil.authUserId());
    }

    public Meal get(int mealId) {
        return service.get(mealId, SecurityUtil.authUserId());
    }

    public List<Meal> getAll(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime)  {
        log.info("getAll with userId={}", SecurityUtil.authUserId());
        return service.getAll(SecurityUtil.authUserId(), startDate, endDate, startTime, endTime);
    }

    public List<Meal> getAll()  {
        log.info("getAll with userId={}", SecurityUtil.authUserId());
        return service.getAll(SecurityUtil.authUserId());
    }

}