package com.richie.model;

public interface Taxable {
    double taxRate = 0.07;

    double getSubtotal();

    default double getTax() {
        return getSubtotal() * taxRate;
    }

    default double getTotal() {
        return getSubtotal() + getTax();
    }
}
