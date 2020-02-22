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


    public static final Meal MEAL_1 = new Meal(
            1,
            LocalDateTime.parse("2020-02-12T07:30:00"),
            "Завтрак",
            1500);

    public static final Meal MEAL_2 = new Meal(
            2,
            LocalDateTime.parse("2020-02-12T13:30:00"),
            "Обед",
            1500);

    public static final Meal MEAL_3 = new Meal(
            3,
            LocalDateTime.parse("2020-02-12T20:30:00"),
            "Ужин",
            1500);

    public static final Meal MEAL_4 = new Meal(
            4,
            LocalDateTime.parse("2020-02-13T07:30:00"),
            "Завтрак",
            500);

    public static final Meal MEAL_6 = new Meal(
            6,
            LocalDateTime.parse("2020-02-13T20:30:00"),
            "Ужин",
            100);

    public static final Meal MEAL_5 = new Meal(
            5,
            LocalDateTime.parse("2020-02-13T13:30:00"),
            "Обед",
            1000);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.parse("2020-02-22T07:30:00"), "Завтрак", 1500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(MEAL_1);
        updated.setDateTime(LocalDateTime.parse("2020-02-12T08:00:00"));
        updated.setDescription("ЗАВТРАК");
        updated.setCalories(555);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorOnFields("id","dateTime", "description", "calories").isEqualTo(expected);
    }


}
