package com.richie.util;

public class test {
    public static void main(String[] args) {
        System.out.println(PriceFormatter.format(12.50));
        System.out.println(PriceFormatter.applyDiscount(15, 10));
    }
}