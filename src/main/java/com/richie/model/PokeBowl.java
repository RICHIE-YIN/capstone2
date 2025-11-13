package com.richie.model;

import java.util.ArrayList;

public class PokeBowl extends Product implements Customizable {
    private String base;
    private String size;
    private ArrayList<Topping> toppings;
    private ArrayList<Extra> extras;

    public PokeBowl(String name, String base, String size) {
        super(name);
        this.base = base;
        this.size = size;
        this.toppings = new ArrayList<>();
        this.extras = new ArrayList<>();
    }

    public void addExtra(Extra extra) {
        extras.add(extra);
    }

    public ArrayList<Extra> getExtras() {
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
        if (size.equalsIgnoreCase("S")) {
            total = 9.50;
        } else if (size.equalsIgnoreCase("M")) {
            total = 12.00;
        } else if (size.equalsIgnoreCase("L")) {
            total = 15.50;
        }

        for (Topping t : toppings) {
            total += t.getPrice();
        }

        for (Extra e : extras) {
            total += e.getPrice();
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