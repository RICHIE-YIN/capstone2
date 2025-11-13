package com.richie.model;

public class Drink extends Product{
    private String size;
    private String flavor;

    public Drink(String size, String flavor) {
        super(flavor);
        this.size = size;
        this.flavor = flavor;
    }

    @Override
    public double getPrice() {
        double basePrice = 4;
        if(size.equalsIgnoreCase("small") || size.equalsIgnoreCase("s")) {
            basePrice = basePrice;
        } else if (size.equalsIgnoreCase("medium") || size.equalsIgnoreCase("m")) {
            basePrice = basePrice * 1.5;
        } else if (size.equalsIgnoreCase("large") || size.equalsIgnoreCase("l")) {
            basePrice = basePrice * 2;
        }
        return basePrice;
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