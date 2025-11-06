package com.richie.model;

import java.util.ArrayList;

public class PokeBowl extends Product implements Customizable {
    private String base;
    private String size;
    private ArrayList<Topping> toppings;
    private ArrayList<String> extras;

    public PokeBowl(String name, String base, String size) {
        super(name);
        this.base = base;
        this.size = size;
        this.toppings = new ArrayList<>();
        this.extras = new ArrayList<>();
    }

    @Override
    public void addExtra(String extra) {
        extras.add(extra);
    }

    @Override
    public ArrayList<String> getExtras() {
        return extras;
    }

    @Override
    public boolean hasExtras() {
        if(extras.isEmpty()) {
            return false;
        }
        return true;
    }

    public void displayDetails(Product product) {
        System.out.println("Name: " + product.getName());
        System.out.println("Price: " + product.getPrice());

        if(product instanceof PokeBowl) {
            PokeBowl pokeBowl = (PokeBowl) product;
            System.out.println("Base: " + pokeBowl.getBase());
        } else if (product instanceof Drink) {
            Drink drink = (Drink) product;
            System.out.println("Size: " + drink.getSize());
        }
    }

    public void addTopping (Topping t) {
        toppings.add(t);
    }

    @Override
    public double getPrice() {
        double total = 0;
        if (size.equals("S")) {
            total = 9.50;
        } else if (size.equals("M")) {
            total = 12.00;
        } else if (size.equals("L")) {
            total = 15.50;
        }

        for (Topping t : toppings) {
            total += t.getPrice();
        }

        return total;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
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
