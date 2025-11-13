package com.richie.model;

public abstract class Product {
    protected String name;

    public Product(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract double getPrice();

    public void displayInfo() {
        System.out.println(name + ": " + getPrice());
    }
}