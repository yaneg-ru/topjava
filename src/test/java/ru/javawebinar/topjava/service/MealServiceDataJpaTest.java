package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.datajpa.DataJpaMealRepository;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles("datajpa")
public class MealServiceDataJpaTest extends MealServiceTest {

    @Autowired
    DataJpaMealRepository dataJpaMealRepository;

    @Test
    public void getWithMeals() throws Exception {
        Meal actual = dataJpaMealRepository.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, ADMIN_MEAL1);
        USER_MATCHER.assertMatch(actual.getUser(), ADMIN);
    }

}