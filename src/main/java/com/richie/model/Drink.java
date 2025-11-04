package com.richie.model;

public class Drink extends Product{
    private String size;
    private String flavor;

    public Drink(String name, double price, String size, String flavor) {
        super(name, price);
        this.size = size;
        this.flavor = flavor;
    }

    public Drink(String name, double price, String size) {
        super(name, price);
        this.size = size;
        this.flavor = "Water";
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }
}
