package com.richie.model;

import java.util.ArrayList;

public class Sandwich extends Product {
    private String breadType;
    private String size;
    private ArrayList<Topping> toppings;

    public Sandwich(String name, String breadType, String size) {
        super(name, 0.00);
        this.breadType = breadType;
        this.size = size;
        this.toppings = new ArrayList<>();
    }

    public void addTopping (Topping t) {
        toppings.add(t);
    }

    public double getPrice() {
        double total = 0;
        if (size.equals("4")) {
            total = 5.50;
        } else if (size.equals("8")) {
            total = 7.00;
        } else if (size.equals("12")) {
            total = 8.50;
        }

        for (Topping t : toppings) {
            total += t.getPrice();
        }

        return total;
    }

    public String getBreadType() {
        return breadType;
    }

    public void setBreadType(String breadType) {
        this.breadType = breadType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public ArrayList<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(ArrayList<Topping> toppings) {
        this.toppings = toppings;
    }
}
