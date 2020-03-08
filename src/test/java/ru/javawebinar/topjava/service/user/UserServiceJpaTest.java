package ru.javawebinar.topjava.service.user;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.JPA)
public class UserServiceJpaTest extends UserServiceTest {

    @Test (expected = UnsupportedOperationException.class)
    @Override
    public void getWithMeals() throws Exception {
        super.getWithMeals();
    }
}