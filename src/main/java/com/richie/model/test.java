package com.richie.model;

import com.richie.util.ReceiptFileManager;

public class test {
    public static void main(String[] args) {
        Order order = new Order();

        Sandwich sando1 = new Sandwich("BLT", "9 Grain Wheat", "8");
        Sandwich sando2 = new Sandwich("Classic Italian", "Italian Herbs and Cheese", "12");
        Drink marg = new Drink("Margarita", "small", "Margarita");
        Chips bbq = new Chips("BBQ Chips");
        Chips cheddar = new Chips("Cheddar Chips");

        Topping bacon = new Topping("Bacon", 1, true);
        Topping ham = new Topping("Ham", .50, true);
        Topping cheese = new Topping("Cheese", .25, true);
        Topping pickle = new Topping("Pickle", .10, false);
        Topping olives = new Topping("Olives", 0, false);
        Topping lettuce = new Topping("Lettuce", 0.25, false);
        Topping tomato = new Topping("Tomato", 0.25, false);
        Topping truffleButter = new Topping("Truffle Butter", 0.50, true);
        Topping italianDressing = new Topping("Italian Dressing", 0.25, false);

        sando1.addTopping(bacon);
        sando1.addTopping(lettuce);
        sando1.addTopping(tomato);
        sando1.addTopping(truffleButter);

        sando2.addTopping(bacon);
        sando2.addTopping(ham);
        sando2.addTopping(cheese);
        sando2.addTopping(pickle);
        sando2.addTopping(olives);
        sando2.addTopping(lettuce);
        sando2.addTopping(tomato);
        sando2.addTopping(truffleButter);
        sando2.addTopping(italianDressing);

        order.addItem(sando1);
        order.addItem(sando2);
        order.addItem(marg);
        order.addItem(bbq);
        order.addItem(cheddar);

        System.out.printf("Subtotal: %.2f\n", order.getSubtotal());
        System.out.printf("Tax: %.2f\n", order.getTax());
        System.out.printf("Total: %.2f", order.getTotal());
        System.out.println("\n");

        ReceiptFileManager.previewReceipt(order);
        ReceiptFileManager.saveReceipt(order);
    }
}
