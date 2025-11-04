package com.richie.model;

import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        ArrayList<Product> items = new ArrayList<>();

        items.add(new Sandwich("JaWhite", "White", "4"));
        items.add(new Drink("coke", "medium"));
        items.add(new Chips("BBQ"));

        double total = 0;
        for(Product p : items) {
            total += p.getPrice();
        }

        System.out.println(total);
    }
}
