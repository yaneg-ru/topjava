package ru.javawebinar.topjava.service.jdbc;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles(Profiles.JDBC)
public  class MealServiceJdbcTest extends MealServiceTest {

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void getWithUser() throws Exception {
        super.getWithUser();
    }
}