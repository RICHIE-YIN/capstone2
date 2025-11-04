package com.richie.model;
import java.util.ArrayList;

public class Order {
    private ArrayList<Sandwich> sandwiches;

    public Order() {
        this.sandwiches = new ArrayList<>();
    }

    public void addItem(Sandwich s) {
        sandwiches.add(s);
    }

    public double getSubtotal() {
        double subTotal = 0;
        for(Sandwich s : sandwiches) {
            subTotal += s.getPrice();
        }
        return subTotal;
    }

    public ArrayList<Sandwich> findSandwichByBread(String breadType) {
        ArrayList<Sandwich> results = new ArrayList<>();
        for(Sandwich s : sandwiches) {
            if(s.getBreadType().equalsIgnoreCase(breadType)) {
                results.add(s);
            }
        }
        return results;
    }

    public Sandwich findSandwichWithTopping(String toppingName) {
        for(Sandwich s : sandwiches) {
            for(Topping t : s.getToppings()) {
                if(t.getName().equalsIgnoreCase(toppingName)) {
                    return s;
                }
            }
        }
        return null;
    }

    public ArrayList<Sandwich> getSandwichOverPrice(double price) {
        ArrayList<Sandwich> results = new ArrayList<>();
        for(Sandwich s : sandwiches) {
            if(s.getPrice() > price) {
                results.add(s);
            }
        }
        return results;
    }

    public ArrayList<Sandwich> getSandwichUnderPrice (double price) {
        ArrayList<Sandwich> results = new ArrayList<>();
        for(Sandwich s : sandwiches) {
            if(s.getPrice() < price) {
                results.add(s);
            }
        }
        return results;
    }

    public void removeToppingFromAllSandwiches(String topping) {
        for(Sandwich s : sandwiches) {
            ArrayList<Topping> toRemove = new ArrayList<>();
            for(Topping t : s.getToppings()) {
                if(t.getName().equalsIgnoreCase(topping)) {
                    toRemove.add(t);
                }
            }

            for(Topping t : toRemove) {
                s.getToppings().remove(t);
            }
        }
    }
}
