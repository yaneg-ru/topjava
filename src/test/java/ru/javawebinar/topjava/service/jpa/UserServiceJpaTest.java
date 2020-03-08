package ru.javawebinar.topjava.service.jpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserServiceTest;

@ActiveProfiles(Profiles.JPA)
public class UserServiceJpaTest extends UserServiceTest {

    @Test (expected = UnsupportedOperationException.class)
    @Override
    public void getWithMeals() throws Exception {
        super.getWithMeals();
    }
}