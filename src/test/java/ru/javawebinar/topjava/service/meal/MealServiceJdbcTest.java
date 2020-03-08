package ru.javawebinar.topjava.service.meal;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.JDBC)
public  class MealServiceJdbcTest extends MealServiceTest {

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void getWithUser() throws Exception {
        super.getWithUser();
    }
}