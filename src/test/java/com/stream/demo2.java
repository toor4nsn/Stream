package com.stream;


import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author Liwei
 * @Date 2021/7/3 16:05
 */

public class demo2 {
    @Test
    public void test() {
//        List<Integer> list1 = Arrays.asList(1, 2, 3);
//        List<Integer> list2 = Arrays.asList(3, 4, 5, 6);
//        List<List<Integer>> collect = Stream.of(list1, list2).collect(Collectors.toList());
//        System.out.println(collect);//[[1, 2, 3], [3, 4, 5, 6]]
        List<String> stringList = Arrays.asList("a", "b", "c", "d", "e");
        List<String> collect = stringList.stream().map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(collect);
        List<String> stringList1 = Arrays.asList("liwei", "zhangli");
//        stringList1.stream().map(demo2::filterCharacter).forEach(x->x.forEach(System.out::println));
        stringList1.stream().flatMap(demo2::filterCharacter).forEach(System.out::println);
    }

    public static Stream<Character> filterCharacter(String str) {
        List<Character> list = new ArrayList<>();
        for (char c : str.toCharArray()) {
            list.add(c);
        }
        return list.stream();
    }

/*    @Test
    public void test2() {
        Student a = Student.builder().id(1).age(25).name("a").build();
        Student b = Student.builder().id(2).age(26).name("b").build();
        Student d = Student.builder().id(3).age(27).name("d").build();
        Student c = Student.builder().id(4).age(27).name("c").build();
        ArrayList<Student> list = new ArrayList<>();
        Collections.addAll(list, a, b, d, c);
        List<Student> collect = list.stream().sorted(Comparator.comparing(Student::getAge).thenComparing(Student::getName, Comparator.reverseOrder())).collect(Collectors.toList());
        System.out.println(collect);
        System.out.println("=====================");
        List<Student> collect2 = list.stream().sorted(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                int num = o1.getAge() - o2.getAge();
                int num2 = num == 0 ? o2.getName().compareTo(o1.getName()) : num;
                return num2;
            }
        }).collect(Collectors.toList());
        System.out.println(collect2);
    }

    @Test
    public void test3() {
        Student a = Student.builder().id(1).age(25).name("a").build();
        Student b = Student.builder().id(2).age(25).name("b").build();
        Student d = Student.builder().id(3).age(26).name("d").build();
        Student c = Student.builder().id(4).age(26).name("c").build();
        ArrayList<Student> list = new ArrayList<>();
        Collections.addAll(list, a, b, d, c);
        boolean b1 = list.stream().allMatch(x -> x.getAge().equals(25));
        System.out.println(b1);
        boolean b2 = list.stream().anyMatch(x -> x.getName().equals("a"));
        System.out.println(b2);
        boolean b3 = list.stream().noneMatch(x -> x.getId().equals(5));
        System.out.println(b3);
        Optional<Student> first = list.stream().findFirst();
        first.ifPresent(System.out::println);
        Optional<Student> any = list.stream().filter(x -> x.getAge().equals(26)).findAny();
        System.out.println(any.get());
        System.out.println("---------------------");
        Optional<Student> max = list.stream().max(Comparator.comparing(Student::getAge).thenComparing(Student::getName));
        max.ifPresent(System.out::println);

    }*/

    @Test
    public void test4() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Integer integer = list.stream()
                .reduce(0, (x, y) -> x + y);
        System.out.println(integer);
    }

/*    @Test
    public void test5() {
        Student a = Student.builder().id(1).age(25).name("a").build();
        Student b = Student.builder().id(2).age(25).name("b").build();
        Student d = Student.builder().id(3).age(26).name("d").build();
        Student c = Student.builder().id(4).age(26).name("c").build();
        Student e = Student.builder().id(5).age(27).name("c").build();
        ArrayList<Student> list = new ArrayList<>();
        Collections.addAll(list, a, b, d, c, e);
        HashSet<String> hashSet = list.stream().map(Student::getName).collect(Collectors.toCollection(HashSet::new));
        System.out.println(hashSet);
        LinkedHashSet<String> linkedHashSet = list.stream().map(Student::getName).collect(Collectors.toCollection(LinkedHashSet::new));
        System.out.println(linkedHashSet);
        Double average = list.stream().collect(Collectors.averagingDouble(Student::getId));
        System.out.println(average);
        Double sum = list.stream().collect(Collectors.summingDouble(Student::getId));
        System.out.println(sum);
    }*/

/*    @Test
    public void test6() {
        Student a = Student.builder().id(1).age(25).name("a").build();
        Student b = Student.builder().id(2).age(25).name("b").build();
        Student d = Student.builder().id(3).age(26).name("c").build();
        Student c = Student.builder().id(4).age(26).name("c").build();
        Student e = Student.builder().id(5).age(27).name("c").build();
        ArrayList<Student> list = new ArrayList<>();
        Collections.addAll(list, a, b, d, c, e);
        Map<String, List<Student>> collect = list.stream().collect(Collectors.groupingBy(Student::getName));
        System.out.println(collect);
        Map<Integer, List<Student>> map = list.stream().collect(Collectors.groupingBy(Student::getId));
        System.out.println(map);

        Map<Integer, Student> map2 = list.stream().collect(Collectors.toMap(Student::getId, Function.identity()));
        System.out.println(map2);
        //条件分组
        Map<Boolean, List<Student>> partition = list.stream().collect(Collectors.partitioningBy(x -> x.getAge() > 25));
        System.out.println(partition);

        String str = list.stream().map(Student::getName).collect(Collectors.joining("-", "#", "#"));
        System.out.println(str);
    }

    @Test
    public void test7() {
        Integer[] arr = {1, 2, 3, 4, 5};
        Arrays.stream(arr).map(x -> x * x).forEach(System.out::println);
        Student a = Student.builder().id(1).age(25).name("a").build();
        Student b = Student.builder().id(2).age(25).name("b").build();
        Student d = Student.builder().id(3).age(26).name("c").build();
        Student c = Student.builder().id(4).age(26).name("c").build();
        Student e = Student.builder().id(5).age(27).name("c").build();
        ArrayList<Student> list = new ArrayList<>();
        Collections.addAll(list, a, b, d, c, e);
        Long count = list.stream().collect(Collectors.counting());
        System.out.println("-------");
        System.out.println(count);

    }*/

    @Test
    public void test8(){
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 1, 3, 3, 2, 4);
        List<Integer> res = list.stream().filter(i -> i % 2 == 0).distinct().collect(Collectors.toList());
        System.out.println(res);
    }
}
