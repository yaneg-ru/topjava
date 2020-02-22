package ru.javawebinar.topjava.repository.jdbc;

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
import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcMealRepositoryTest {

    @Autowired
    JdbcMealRepository repository;

    @Test
    public void save() {
        Meal newMeal = getNew();
        Meal savedMeal = repository.save(newMeal,USER_ID);
        int newId = savedMeal.getId();
        newMeal.setId(newId);
        assertMatch(repository.get(newId,USER_ID), newMeal);
    }

    @Test (expected = NullPointerException.class)
    public void updateMealOtherUser() {
        repository.save(MEAL_1,ADMIN_ID).isNew();
    }

    @Test
    public void delete() {
        boolean resultDelete = repository.delete(1,USER_ID);
        assertThat(resultDelete).isEqualTo(true);
        resultDelete = repository.delete(2,ADMIN_ID);
        assertThat(resultDelete).isEqualTo(false);
    }

    @Test
    public void get() {
        assertMatch(repository.get(1, USER_ID), MEAL_1);
        assertMatch(repository.get(6, ADMIN_ID), MEAL_6);
    }

    @Test
    public void getAll() {
        List<Meal> mealList = repository.getAll(USER_ID);
        assertThat(mealList).containsOnly(MEAL_1, MEAL_2, MEAL_3);
        mealList = repository.getAll(ADMIN_ID);
        assertThat(mealList).containsOnly(MEAL_4, MEAL_5, MEAL_6);
    }

    @Test
    public void getBetweenHalfOpen() {
        List<Meal> mealList = repository.getBetweenHalfOpen(LocalDateTime.parse("2020-02-12T00:00:00"), LocalDateTime.parse("2020-02-13T00:00:00"), USER_ID);
        assertThat(mealList).containsOnly(MEAL_1, MEAL_2, MEAL_3);
    }
}