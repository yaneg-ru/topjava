package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(1, USER_ID);
        assertMatch(meal, MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        mealService.get(1, ADMIN_ID);
    }

    @Test (expected = NotFoundException.class)
    public void delete() {
        mealService.delete(1, USER_ID);
        mealService.get(1, USER_ID);
    }

    @Test
    public void getBetweenHalfOpen() {
        List<Meal> mealList = mealService.getBetweenHalfOpen(LocalDate.parse("2020-02-12"), LocalDate.parse("2020-02-13"), USER_ID);
        assertMatch(mealList, MEAL_3, MEAL_2, MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception {
        mealService.delete(1, ADMIN_ID);
    }

    @Test
    public void getAll() {
        List<Meal> mealList = mealService.getAll(ADMIN_ID);
        assertMatch(mealList, MEAL_6, MEAL_5, MEAL_4);
    }


    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal updated = getUpdated();
        mealService.update(updated, ADMIN_ID);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        mealService.update(updated, USER_ID);
        assertMatch(mealService.get(1, USER_ID), updated);
    }

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal created = mealService.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(mealService.get(newId,USER_ID), newMeal);    }
}
