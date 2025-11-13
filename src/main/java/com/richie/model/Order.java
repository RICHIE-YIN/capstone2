package com.richie.model;
import java.util.ArrayList;

public class Order implements Taxable {
    private ArrayList<Product> items;
    private String name;

    public Order(String name) {
        this.name = name;
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

            if(p instanceof PokeBowl) {
                PokeBowl s = (PokeBowl) p;
                System.out.println("Base: " + s.getBase());
            } else if(p instanceof Drink) {
                Drink d = (Drink) p;
                System.out.println("Drink size: " + d.getSize());
            }
        }
    }

    public ArrayList<Product> getItems() {
        return items;
    }

    public void setItems(ArrayList<Product> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}