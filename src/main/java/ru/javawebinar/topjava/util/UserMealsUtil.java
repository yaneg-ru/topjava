package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;
import ru.javawebinar.topjava.model.UserMealWithExcessVer2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 12, 0), "Второй завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        printBreakLine("filteredByCycles");
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        printBreakLine("filteredByCyclesOptional_1");
        mealsTo = filteredByCyclesOptional_1(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        printBreakLine("filteredByCyclesOptional_2");
        List<UserMealWithExcessVer2> mealsToVer2 = filteredByCyclesOptional_2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        printBreakLine("filteredByStreams");
        mealsTo = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        if (meals == null || startTime == null || endTime == null) return null;

        Map<LocalDate, Integer> totalUserCaloriesPerDay = new HashMap<>();
        final LocalDate[] keyMapLocalDate = new LocalDate[1];

        meals.forEach(userMeal -> totalUserCaloriesPerDay.merge(
                userMeal.getDateTime().toLocalDate(),
                userMeal.getCalories(),
                Integer::sum
        ));

        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();

        meals.forEach(userMeal -> {
            if (TimeUtil.isBetweenInclusive(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                Integer totalCaloriesPerDay = totalUserCaloriesPerDay.get(userMeal.getDateTime().toLocalDate());
                userMealWithExcesses.add(
                        new UserMealWithExcess(
                                userMeal.getDateTime(),
                                userMeal.getDescription(),
                                userMeal.getCalories(), (totalCaloriesPerDay > caloriesPerDay)
                        ));
            }
        });

        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByCyclesOptional_1(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        if (meals == null || startTime == null || endTime == null) return null;

        meals.sort(Comparator.comparing(o -> o.getDateTime().toLocalDate()));

        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        int firstIndexLastDate = 0;

        int totalUserCaloriesPerDay = 0;
        boolean needAdd;
        LocalDate lastDate = meals.get(0).getDateTime().toLocalDate();

        for (int i = 0; i < meals.size(); i++) {
            totalUserCaloriesPerDay += meals.get(i).getCalories();

            needAdd = TimeUtil.isBetweenInclusive(meals.get(i).getDateTime().toLocalTime(), startTime, endTime);

            if (i > meals.size() - 1 &&
                    lastDate.compareTo(meals.get(i + 1).getDateTime().toLocalDate()) != 0) {

                if (totalUserCaloriesPerDay > caloriesPerDay) {
                    for (int j = firstIndexLastDate; j < userMealWithExcesses.size(); j++) {
                        userMealWithExcesses.get(j).setExcess(true);
                    }
                    firstIndexLastDate = userMealWithExcesses.size();
                }

                totalUserCaloriesPerDay = 0;
                lastDate = meals.get(i + 1).getDateTime().toLocalDate();
            }

            if (needAdd) userMealWithExcesses.add(
                    new UserMealWithExcess(
                            meals.get(i).getDateTime(),
                            meals.get(i).getDescription(),
                            meals.get(i).getCalories(),
                            (totalUserCaloriesPerDay > caloriesPerDay)
                    ));
        }

        return userMealWithExcesses;
    }

    public static List<UserMealWithExcessVer2> filteredByCyclesOptional_2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        if (meals == null || startTime == null || endTime == null) return null;

        List<UserMealWithExcessVer2> userMealWithExcesses = new ArrayList<>();

        int totalUserCaloriesPerDay = 0;
        boolean needAdd;
        LocalDate lastDate = meals.get(0).getDateTime().toLocalDate();

        for (int i = 0; i < meals.size(); i++) {

            totalUserCaloriesPerDay += meals.get(i).getCalories();
            if (totalUserCaloriesPerDay > caloriesPerDay) {
                UserMealWithExcessVer2.excessPerDays.put(meals.get(i).getDateTime().toLocalDate(), true);
            } else {
                UserMealWithExcessVer2.excessPerDays.put(meals.get(i).getDateTime().toLocalDate(), false);
            }

            needAdd = TimeUtil.isBetweenInclusive(meals.get(i).getDateTime().toLocalTime(), startTime, endTime);

            if (i > meals.size() - 1 && lastDate.compareTo(meals.get(i + 1).getDateTime().toLocalDate()) != 0) {
                totalUserCaloriesPerDay = 0;
                lastDate = meals.get(i + 1).getDateTime().toLocalDate();
            }

            if (needAdd) userMealWithExcesses.add(
                    new UserMealWithExcessVer2(
                            meals.get(i).getDateTime(),
                            meals.get(i).getDescription(),
                            meals.get(i).getCalories()
                    ));
        }

        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        if (meals == null || startTime == null || endTime == null) return null;

        Map<LocalDate, Integer> totalUserCaloriesPerDay = meals.stream()
                .collect(toMap(
                        userMeal -> userMeal.getDateTime().toLocalDate(),
                        UserMeal::getCalories,
                        Integer::sum)
                );

        List<UserMealWithExcess> userMealWithExcesses;
        userMealWithExcesses = meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenInclusive(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExcess(
                                userMeal.getDateTime(),
                                userMeal.getDescription(),
                                userMeal.getCalories(),
                                (totalUserCaloriesPerDay.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay)
                        )
                )
                .collect(Collectors.toList());

        return userMealWithExcesses;
    }

    public static void printBreakLine(String s) {
        System.out.println();
        System.out.format("--------- %s ---------\n", s);
        System.out.println();
    }
}
