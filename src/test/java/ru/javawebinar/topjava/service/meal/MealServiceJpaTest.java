package ru.javawebinar.topjava.service.meal;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.JPA)
public  class MealServiceJpaTest extends MealServiceTest {

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void getWithUser() throws Exception {
        super.getWithUser();
    }
}