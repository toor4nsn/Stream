package com.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Liwei
 * @Date 2021/8/15 21:48
 */
public class Demo3 {
    public static void main(String[] args) {
        List<? super Human> humanList = new ArrayList<>();
        humanList.add(new Human("人类"));
        //humanList=new ArrayList<Chinese>(); //Error
        humanList=new ArrayList<Primate>();
        humanList=new ArrayList<Creature>();


        humanList.add(new Human("女性"));
        humanList.add(new Chinese("中国人"));

//        humanList.add(new Primate("灵长类动物"));//Error
//        humanList.add(new Creature("生物"));//Error
//        humanList.add("无关类型,如String");//Error

        Object object = humanList.get(0);

        List<Creature> creatureList=new ArrayList<>();
        creatureList.add(new Human("人类"));

    }


    static class Creature {
        public Creature(String name) {
            this.name = name;
        }

        private String name;
    }

    static class Primate extends Creature {
        public Primate(String name) {
            super(name);
        }
    }

    static class Human extends Primate {
        public Human(String name) {
            super(name);
        }
    }

    static class Chinese extends Human {
        public Chinese(String name) {
            super(name);
        }
    }

    static class Japanese extends Human {
        public Japanese(String name) {
            super(name);
        }
    }
}
