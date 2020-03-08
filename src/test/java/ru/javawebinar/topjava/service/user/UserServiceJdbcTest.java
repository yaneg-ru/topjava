package ru.javawebinar.topjava.service.user;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.JDBC)
public class UserServiceJdbcTest extends UserServiceTest {

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void getWithMeals() throws Exception {
        super.getWithMeals();
    }
}