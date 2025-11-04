package com.richie.util;

public class PriceFormatter {
    private PriceFormatter() {}

    public static String format(double price) {
        return String.format("$%.2f", price);
    }

    public static double applyDiscount(double price, double percentOff) {
        return price * (1 - percentOff / 100);
    }
}
