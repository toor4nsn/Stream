package com.lambdasinaction.chap6;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static com.lambdasinaction.chap6.Dish.menu;

public class Reducing {

    public static void main(String... args) {
        System.out.println("Total calories in menu: " + calculateTotalCalories());
        System.out.println("Total calories in menu: " + calculateTotalCaloriesWithMethodReference());
        System.out.println("Total calories in menu: " + calculateTotalCaloriesWithoutCollectors());
        System.out.println("Total calories in menu: " + calculateTotalCaloriesUsingSum());
    }

    private static int calculateTotalCalories() {
        return menu.stream().collect(reducing(0, Dish::getCalories, (Integer i, Integer j) -> i + j));
    }

    private static int calculateTotalCaloriesWithMethodReference() {
        return menu.stream().collect(reducing(0, Dish::getCalories, Integer::sum));
    }

    private static int calculateTotalCaloriesWithoutCollectors() {
        return menu.stream().map(Dish::getCalories).reduce(Integer::sum).get();
    }

    private static int calculateTotalCaloriesUsingSum() {
        return menu.stream().mapToInt(Dish::getCalories).sum();
    }

    @Test
    public void test() {
        Integer sum = menu.stream().collect(reducing(0, Dish::getCalories, (i, j) -> i + j));
        System.out.println(sum);
        Optional<Dish> max = menu.stream().collect(reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
        max.ifPresent(System.out::println);

        Optional<Dish> max2 = menu.stream().max(Comparator.comparingInt(Dish::getCalories));
        max2.ifPresent(System.out::println);

        System.out.println(menu.stream().sorted(Comparator.comparingInt(Dish::getCalories).reversed()).limit(1).collect(toList()));
        Integer sum2 = menu.stream().collect(reducing(0, Dish::getCalories, Integer::sum));
//        System.out.println(sum2);
        int sum3 = menu.stream().mapToInt(Dish::getCalories).sum();
        System.out.println(sum3);

    }

}