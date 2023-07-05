package com.lambdasinaction.chap5;

import cn.hutool.core.collection.TransCollection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

public class PuttingIntoPractice{
    public static void main(String ...args){    
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");
		
		List<Transaction> transactions = Arrays.asList(
            new Transaction(brian, 2011, 300), 
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),	
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950)
        );	
        
        
        // Query 1: Find all transactions from year 2011 and sort them by value (small to high).
        List<Transaction> tr2011 = transactions.stream()
                                               .filter(transaction -> transaction.getYear() == 2011)
                                               .sorted(comparing(Transaction::getValue))
                                               .collect(toList());
        System.out.println(tr2011);
        
        // Query 2: What are all the unique cities where the traders work?
        List<String> cities = 
            transactions.stream()
                        .map(transaction -> transaction.getTrader().getCity())
                        .distinct()
                        .collect(toList());
        System.out.println(cities);



        // Query 3: Find all traders from Cambridge and sort them by name.
        
        List<Trader> traders = 
            transactions.stream()
                        .map(Transaction::getTrader)
                        .filter(trader -> trader.getCity().equals("Cambridge"))
                        .distinct()
                        .sorted(comparing(Trader::getName))
                        .collect(toList());
        System.out.println(traders);
        
        
        // Query 4: Return a string of all traders’ names sorted alphabetically.
        
        String traderStr = 
            transactions.stream()
                        .map(transaction -> transaction.getTrader().getName())
                        .distinct()
                        .sorted()
                        .reduce("", (n1, n2) -> n1 + n2);
        System.out.println(traderStr);
        
        // Query 5: Are there any trader based in Milan?
        
        boolean milanBased =
            transactions.stream()
                        .anyMatch(transaction -> transaction.getTrader()
                                                            .getCity()
                                                            .equals("Milan")
                                 );
        System.out.println(milanBased);
        
        
        // Query 6: Update all transactions so that the traders from Milan are set to Cambridge.
        transactions.stream()
                    .map(Transaction::getTrader)
                    .filter(trader -> trader.getCity().equals("Milan"))
                    .forEach(trader -> trader.setCity("Cambridge"));
        System.out.println(transactions);
        
        
        // Query 7: What's the highest value in all the transactions?
        int highestValue = 
            transactions.stream()
                        .map(Transaction::getValue)
                        .reduce(0, Integer::max);
        System.out.println(highestValue);


        System.out.println("----------------------------------------------------------------");
        Integer max = transactions.stream().map(Transaction::getValue).reduce(0, (x, y) -> x > y ? x : y);
        System.out.println(max);
        Optional<Integer> max2 = transactions.stream().max(comparingInt(Transaction::getValue)).map(Transaction::getValue);
        System.out.println(max2.orElse(0));
        System.out.println("----------------------------------------------------------------");

        Optional<Transaction> minTransction = transactions.stream().reduce((x, y) -> x.getValue() < y.getValue() ? x : y);

        OptionalInt min = transactions.stream().mapToInt(Transaction::getValue).min();
        min.ifPresent(System.out::println);


    }




    @Test
    public void test(){
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );
        System.out.println(transactions.stream().filter(transaction -> transaction.getYear() == 2011).sorted(comparing(Transaction::getValue)).collect(toList()));
        System.out.println(transactions.stream().map(item -> item.getTrader().getCity()).distinct().collect(toList()));
        System.out.println(transactions.stream().map(Transaction::getTrader).filter(trader -> "Cambridge".equals(trader.getCity())).distinct().sorted(comparing(Trader::getName)).collect(toList()));
        System.out.println(transactions.stream().map(item -> item.getTrader().getName()).distinct().sorted().reduce("", (a, b) -> a + b));
        boolean milan = transactions.stream().anyMatch(item -> item.getTrader().getCity().equals("Milan"));
        System.out.println(milan);

        transactions.stream().filter(item -> item.getTrader().getCity().equals("Cambridge")).forEach(System.out::println);

        Optional<Integer> max = transactions.stream().map(Transaction::getValue).max(Integer::compareTo);
        max.ifPresent(System.out::println);
        System.out.println(transactions.stream().map(Transaction::getValue).reduce(0, Integer::max));

        Optional<Integer> min = transactions.stream().map(Transaction::getValue).min(Integer::compareTo);
        min.ifPresent(System.out::println);


    }

}