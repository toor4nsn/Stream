package com.lambdasinaction.chap5;
import com.lambdasinaction.chap4.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.*;
import java.util.*;

import static com.lambdasinaction.chap4.Dish.menu;

public class NumericStreams{

    public static void main(String...args){
    
        List<Integer> numbers = Arrays.asList(3,4,5,1,2);

        Arrays.stream(numbers.toArray()).forEach(System.out::println);
        int calories = menu.stream()
                           .mapToInt(Dish::getCalories)
                           .sum();
        System.out.println("Number of calories:" + calories);


        // max and OptionalInt
        OptionalInt maxCalories = menu.stream()                                                      
                                      .mapToInt(Dish::getCalories)
                                      .max();

        int max;
        if(maxCalories.isPresent()){
            max = maxCalories.getAsInt();
        }
        else {
            // we can choose a default value
            max = 1;
        }
        System.out.println(max);

        // numeric ranges
        IntStream evenNumbers = IntStream.rangeClosed(1, 100)
                                 .filter(n -> n % 2 == 0);

        System.out.println(evenNumbers.count());

        Stream<int[]> pythagoreanTriples =
               IntStream.rangeClosed(1, 100).boxed()
                        .flatMap(a -> IntStream.rangeClosed(a, 100)
                                               .filter(b -> Math.sqrt(a*a + b*b) % 1 == 0).boxed()
                                               .map(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)}));       

        pythagoreanTriples.forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2])); 

    }
   
    public static boolean isPerfectSquare(int n){
        return Math.sqrt(n) % 1 == 0;
    }


    @Test
    public void intStreamTest(){
        long count = IntStream.rangeClosed(1, 100).filter(i -> i % 2 == 0).count();
        System.out.println(count);
    }

    @Test
    public void iterateTest(){
        List<Integer> even = Stream.iterate(0, n -> n + 2).limit(10).collect(Collectors.toList());
        System.out.println(even);
        List<Integer> odd = Stream.iterate(1, n -> n + 2).limit(10).collect(Collectors.toList());
        System.out.println(odd);
        LocalDate start = LocalDate.of(2022, 11, 1);
        LocalDate end = LocalDate.of(2022, 12, 31);
        List<LocalDate> interval = calculateLocalDate(start, end);
        System.out.println(interval);

        List<Integer> fibo = Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]}).limit(20)
                .map(t -> t[0])
                .collect(Collectors.toList());
        System.out.println(fibo);

    }

    public List<LocalDate> calculateLocalDate(LocalDate start,LocalDate end){
        List<LocalDate> collect = Stream.iterate(start, item -> item.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .collect(Collectors.toList());
        return collect;
    }
}
