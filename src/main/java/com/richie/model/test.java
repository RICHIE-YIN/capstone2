package com.richie.model;

import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        Order order = new Order();

        Sandwich sando1 = new Sandwich("BLT", "9 Grain Wheat", "8");
        Drink marg = new Drink("Margarita", "small", "Margarita");
        Chips bbq = new Chips("BBQ Chips");

        Topping bacon = new Topping("Bacon", 1, true);
        Topping lettuce = new Topping("Lettuce", 0.25, false);
        Topping tomato = new Topping("Tomato", 0.25, false);
        Topping truffleButter = new Topping("Truffle Butter", 0.50, true);

        sando1.addTopping(bacon);
        sando1.addTopping(lettuce);
        sando1.addTopping(tomato);
        sando1.addTopping(truffleButter);

        order.addItem(sando1);
        order.addItem(marg);
        order.addItem(bbq);

        System.out.printf("Subtotal: %.2f\n", order.getSubtotal());
        System.out.printf("Tax: %.2f\n", order.getTax());
        System.out.printf("Total: %.2f", order.getTotal());
    }
}
