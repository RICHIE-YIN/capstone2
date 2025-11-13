package com.richie.model;

public class Extra {
    private Topping topping;

    public Extra(Topping topping) {
        this.topping = topping;
    }

    public Topping getTopping() {
        return topping;
    }

    public double getUpcharge() {
        if (topping.isPremium()) {
            return 2.00;  // Extra protein
        } else {
            return 0.50;  // Extra regular topping
        }
    }

    public double getPrice() {
        return getUpcharge();
    }

    public String getName() {
        return "Extra " + topping.getName();
    }
}