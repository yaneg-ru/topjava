package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
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

    public void delete(int mealId) {
        log.info("delete {} with userId={}", mealId, SecurityUtil.authUserId());
        service.delete(mealId, SecurityUtil.authUserId());
    }

    public Meal get(int mealId) {
        return service.get(mealId, SecurityUtil.authUserId());
    }

    public List<MealTo> getAllByFiltrs(String sDate, String eDate, String sTime, String eTime) {
        log.info("getAll with userId={}", SecurityUtil.authUserId());

        LocalDate startDate = (sDate.isEmpty() ? LocalDate.MIN : LocalDate.parse(sDate));
        LocalDate endDate = (eDate.isEmpty() ? LocalDate.MAX : LocalDate.parse(eDate));
        LocalTime startTime = (sTime.isEmpty() ? LocalTime.MIN : LocalTime.parse(sTime));
        LocalTime endTime = (eTime.isEmpty() ? LocalTime.MAX : LocalTime.parse(eTime));

        return service.getAllByUserByFilters(getAll(), startDate, endDate, startTime, endTime);
    }

    public List<MealTo> getAll() {
        log.info("getAll with userId={}", SecurityUtil.authUserId());
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

}