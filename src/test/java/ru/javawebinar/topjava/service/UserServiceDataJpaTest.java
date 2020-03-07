package ru.javawebinar.topjava.service;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.datajpa.DataJpaUserRepository;

import java.util.ArrayList;

import static ru.javawebinar.topjava.MealTestData.MEALS;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles("datajpa")
public class UserServiceDataJpaTest extends UserServiceTest {

    @Autowired
    DataJpaUserRepository dataJpaUserRepository;

    @Test
    public void getWithMeals() throws Exception {
        User user = dataJpaUserRepository.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(user, USER);
        ArrayList meals_actual = Lists.newArrayList(user.getMeals());
        ArrayList meals_expected = Lists.newArrayList(MEALS);
        MEAL_MATCHER.assertMatch( meals_actual, meals_expected);
    }
}