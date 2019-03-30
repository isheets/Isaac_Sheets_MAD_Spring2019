package com.example.lab06;

import java.util.ArrayList;
import java.util.List;

public class Fruit {
    //class variables
    private String name;
    private String season;

    //constructor
    public Fruit(String curName, String curSeason) {
        this.name = curName;
        this.season = curSeason;
    }

    public static List<Fruit> fruits = new ArrayList<Fruit>(){{
        add(new Fruit("Grapefruit", "Winter"));
        add(new Fruit("Mango", "Spring"));
        add(new Fruit("Peaches", "Summer"));
        add(new Fruit("Apples", "Fall"));
        add(new Fruit("Grapes", "Fall"));
        add(new Fruit("Lemons", "Winter"));
        add(new Fruit("Pineapple", "Spring"));
        add(new Fruit("Blackberries", "Summer"));
        add(new Fruit("Plums", "Summer"));
        add(new Fruit("Blueberries", "Summer"));
    }};

    public String getName() {
        return name;
    }
    public String getSeason() {
        return season;
    }

}
