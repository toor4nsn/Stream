package com.lambdasinaction.chap5;
import com.lambdasinaction.chap4.*;

import java.util.*;

import static com.lambdasinaction.chap4.Dish.menu;

public class Reducing{

    public static void main(String...args){

        List<Integer> numbers = Arrays.asList(3,4,5,1,2);
        int sum = numbers.stream().reduce(0, (a, b) -> a + b);
        System.out.println(sum);

        int sum2 = numbers.stream().reduce(0, Integer::sum);
        System.out.println(sum2);

        int max = numbers.stream().reduce(0, Integer::max);
        System.out.println(max);

        Optional<Integer> min = numbers.stream().reduce(Integer::min);
        min.ifPresent(System.out::println);

        int calories = menu.stream()
                           .map(Dish::getCalories)
                           .reduce(0, Integer::sum);
        System.out.println("Number of calories:" + calories);


        System.out.println(menu.stream().mapToInt(Dish::getCalories).sum());
        OptionalInt max1 = menu.stream().mapToInt(Dish::getCalories).max();
        max1.ifPresent(System.out::println);
        int max2 = max1.orElse(0);
        System.out.println(max2);


        int[] arr={1,2,3,4,5};
        System.out.println(Arrays.stream(arr).sum());


    }
}
