package ru.javawebinar.topjava.service.meal;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.REPOSITORY_IMPLEMENTATION)
public class MealServiceDataJpaTest extends MealServiceTest {

}