package com.richie.model;

public class Sides extends Product {
    private String type;

    public Sides(String type) {
        super(type);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public double getPrice() {
        return 2.50;
    }

    public void setType(String type) {
        this.type = type;
    }
}
