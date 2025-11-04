package com.richie.model;

public class Chips extends Product {
    private String type;

    public Chips(String type) {
        super(type, 1.50);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return 1.50;
    }

    public void setType(String type) {
        this.type = type;
    }
}
