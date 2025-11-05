package com.richie.model;
import java.util.ArrayList;

public class Order implements Taxable {
    private ArrayList<Product> items;

    public Order() {
        this.items = new ArrayList<>();
    }

    public void addItem(Product p) {
        items.add(p);
    }

    @Override
    public double getSubtotal() {
        double total = 0;
        for(Product p : items) {
            total += p.getPrice();
        }
        return total;
    }

    public void displayAll() {
        for(Product p : items) {
            System.out.println("Name: " + p.getName());
            System.out.println("Price: " + p.getPrice());

            if(p instanceof Sandwich) {
                Sandwich s = (Sandwich) p;
                System.out.println("Bread type: " + s.getBreadType());
            } else if(p instanceof Drink) {
                Drink d = (Drink) p;
                System.out.println("Drink size: " + d.getSize());
            }
        }
    }

//    public double getSubtotal() {
//        double subTotal = 0;
//        for(Sandwich s : sandwiches) {
//            subTotal += s.getPrice();
//        }
//        return subTotal;
//    }
//
//    public ArrayList<Sandwich> findSandwichByBread(String breadType) {
//        ArrayList<Sandwich> results = new ArrayList<>();
//        for(Sandwich s : sandwiches) {
//            if(s.getBreadType().equalsIgnoreCase(breadType)) {
//                results.add(s);
//            }
//        }
//        return results;
//    }
//
//    public Sandwich findSandwichWithTopping(String toppingName) {
//        for(Sandwich s : sandwiches) {
//            for(Topping t : s.getToppings()) {
//                if(t.getName().equalsIgnoreCase(toppingName)) {
//                    return s;
//                }
//            }
//        }
//        return null;
//    }
//
//    public ArrayList<Sandwich> getSandwichOverPrice(double price) {
//        ArrayList<Sandwich> results = new ArrayList<>();
//        for(Sandwich s : sandwiches) {
//            if(s.getPrice() > price) {
//                results.add(s);
//            }
//        }
//        return results;
//    }
//
//    public ArrayList<Sandwich> getSandwichUnderPrice (double price) {
//        ArrayList<Sandwich> results = new ArrayList<>();
//        for(Sandwich s : sandwiches) {
//            if(s.getPrice() < price) {
//                results.add(s);
//            }
//        }
//        return results;
//    }
//
//    public void removeToppingFromAllSandwiches(String topping) {
//        for(Sandwich s : sandwiches) {
//            ArrayList<Topping> toRemove = new ArrayList<>();
//            for(Topping t : s.getToppings()) {
//                if(t.getName().equalsIgnoreCase(topping)) {
//                    toRemove.add(t);
//                }
//            }
//
//            for(Topping t : toRemove) {
//                s.getToppings().remove(t);
//            }
//        }
//    }
}
