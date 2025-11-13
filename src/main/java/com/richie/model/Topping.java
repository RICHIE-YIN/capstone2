package com.richie.model;

public class Topping {
    private static int totalCreated = 0;
    private String name;
    private double basePrice;
    private boolean isPremium;

    public Topping(String name, double basePrice, boolean isPremium) {
        this.name = name;
        this.basePrice = basePrice;
        this.isPremium = isPremium;
        totalCreated++;
    }

    public static int getTotalCreated() {
        return totalCreated;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        if (isPremium) {
            return basePrice * 1.5;
        } else {
            return basePrice;
        }
    }

    public boolean isPremium() {
        return isPremium;
    }
}