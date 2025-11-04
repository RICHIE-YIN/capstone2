package com.richie.model;

public class test {
    public static void main(String[] args) {
        Drink drink1 = new Drink("Red Raz", 5.50, "16", "Rasberry");
        Drink drink2 = new Drink("H2O", 1.00, "20");
        System.out.println("Drink name: " + drink1.getName() + " Size: " + drink1.getSize() + " Flavor: " + drink1.getFlavor() + " Price: " +  drink1.getPrice());
        System.out.println("Drink name: " + drink2.getName() + " Size: " + drink2.getSize() + " Flavor: " + drink2.getFlavor() + " Price: " +  drink2.getPrice());
    }
}
