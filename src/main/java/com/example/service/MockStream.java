package com.example.service;

/**
 * @Author Liwei
 * @Date 2021/7/7 0:29
 */
public class MockStream {
    public static void main(String[] args) {
        Person bravo = new Person("bravo", 18);


        PredicateImpl predicate1 = new PredicateImpl();
        myPrint(bravo, predicate1);


        Predicate<Person> predicate2 = new Predicate<Person>() {
            @Override
            public boolean test(Person person) {
                return person.getAge()<18;
            }
        };
        myPrint(bravo,predicate2);


        Predicate<Person> predicate3 = person -> person.getAge()==18;
        myPrint(bravo,predicate3);
    }
    public static void myPrint(Person person, Predicate<Person> filter) {
        if (filter.test(person)) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }

}
