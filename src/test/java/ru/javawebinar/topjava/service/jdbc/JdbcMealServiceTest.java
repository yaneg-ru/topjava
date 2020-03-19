package ru.javawebinar.topjava.service.jdbc;

import org.junit.Assert;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;

import static ru.javawebinar.topjava.MealTestData.MEAL1;
import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.Profiles.JDBC;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

@ActiveProfiles(JDBC)
public class JdbcMealServiceTest extends AbstractMealServiceTest {
}