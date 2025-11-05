package com.richie.model;

public class Chips extends Product {
    private String type;

    public Chips(String type) {
        super(type);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public double getPrice() {
        return 1.50;
    }

    public void setType(String type) {
        this.type = type;
    }
}
