package com.richie.model;

public class test {
    public static void main(String[] args) {
        Drink drink1 = new Drink("16", "Coke");
        Drink drink2 = new Drink("24");
        System.out.println(drink1.getSize() + ", " + drink1.getFlavor());
        System.out.println(drink2.getSize() + ", " + drink2.getFlavor());
    }
}
