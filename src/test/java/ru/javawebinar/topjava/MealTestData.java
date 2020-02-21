package ru.javawebinar.topjava;


import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    private static int id = 1;
    private static LocalDateTime dateTime = LocalDateTime.parse("2020-02-12T07:30:00");
    private static String description = "Завтрак";
    private static int calories = 1500;


    public static final Meal MEAL = new Meal(
            1,
            LocalDateTime.parse("2020-02-12T07:30:00"),
            "Завтрак",
            1500);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.parse("2020-02-12T07:30:00"), "Завтрак", 1500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(MEAL);
        updated.setDateTime(LocalDateTime.parse("2020-02-12T08:00:00"));
        updated.setDescription("ЗАВТРАК");
        updated.setCalories(555);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        // assertThat(actual).isEqualToIgnoringGivenFields(expected, "userid");
        assertThat(actual).isEqualTo(expected);
    }

//    public static void assertMatch(Iterable<User> actual, User... expected) {
//        assertMatch(actual, Arrays.asList(expected));
//    }
//
//    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
//        assertThat(actual).usingElementComparatorIgnoringFields("registered", "roles").isEqualTo(expected);
//    }
}
