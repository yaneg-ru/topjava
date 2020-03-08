package ru.javawebinar.topjava.service.jpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles(Profiles.JPA)
public  class MealServiceJpaTest extends MealServiceTest {

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void getWithUser() throws Exception {
        super.getWithUser();
    }
}