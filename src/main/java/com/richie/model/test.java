package com.richie.model;

import com.richie.util.ReceiptFileManager;

public class test {
    public static void main(String[] args) {
        Order order = new Order();

        PokeBowl bowl1 = new PokeBowl("Richie's Bowl", "White Rice", "L");
        PokeBowl bowl2 = new PokeBowl("Kayla's Bowl", "White Rice", "S");
        PokeBowl bowl3 = new PokeBowl("Empty Bowl", "White Rice", "M");
        Drink coke = new Drink("small", "coke");
        Sides edamame = new Sides("Edamame");
        Sides takoyaki = new Sides("Takoyaki");

        Topping salmon = new Topping("Salmon", 2.00, true);
        Topping tuna = new Topping("Tuna", 2.25, true);
        Topping spicyTuna = new Topping("Spicy Tuna", 2.50, true);
        Topping spicySalmon = new Topping("Spicy Salmon", 2.50, true);
        Topping shrimp = new Topping("Shrimp", 1.75, true);
        Topping tofu = new Topping("Tofu", 1.00, true);
        Topping crabMix = new Topping("Crab Mix", 1.25, true);
        Topping avocado = new Topping("Avocado", 1.00, true);
        Topping seaweedSalad = new Topping("Seaweed Salad", 0.75, false);
        Topping cucumber = new Topping("Cucumber", 0.25, false);
        Topping mango = new Topping("Mango", 0.50, false);
        Topping greenOnion = new Topping("Green Onion", 0.25, false);
        Topping masago = new Topping("Masago", 0.75, false);
        Topping pickledGinger = new Topping("Pickled Ginger", 0.25, false);
        Topping jalapeno = new Topping("Jalapeño", 0.25, false);
        Topping nori = new Topping("Nori", 0.10, false);
        Topping spicyMayo = new Topping("Spicy Mayo", 0.50, false);
        Topping eelSauce = new Topping("Eel Sauce", 0.50, false);
        Topping ponzuSauce = new Topping("Ponzu Sauce", 0.50, false);
        Topping sesameOil = new Topping("Sesame Oil", 0.25, false);
        Topping yuzuDressing = new Topping("Yuzu Dressing", 0.50, false);
        Topping wasabiAioli = new Topping("Wasabi Aioli", 0.75, false);
        Topping sriracha = new Topping("Sriracha", 0.25, false);
        Topping furikake = new Topping("Furikake", 0.25, false);
        Topping crispyOnions = new Topping("Crispy Onions", 0.50, false);
        Topping tempuraFlakes = new Topping("Tempura Flakes", 0.50, false);

        bowl1.addTopping(spicyTuna);
        bowl1.addTopping(avocado);
        bowl1.addTopping(seaweedSalad);
        bowl1.addTopping(cucumber);
        bowl1.addTopping(greenOnion);
        bowl1.addTopping(masago);
        bowl1.addTopping(spicyMayo);
        bowl1.addTopping(eelSauce);
        bowl1.addTopping(sesameOil);
        bowl1.addTopping(wasabiAioli);
        bowl1.addTopping(furikake);
        bowl1.addTopping(crispyOnions);
        bowl1.addTopping(tempuraFlakes);

        bowl2.addTopping(spicySalmon);
        bowl2.addTopping(crabMix);
        bowl2.addTopping(avocado);
        bowl2.addTopping(seaweedSalad);
        bowl2.addTopping(cucumber);
        bowl2.addTopping(greenOnion);
        bowl2.addTopping(masago);
        bowl2.addTopping(spicyMayo);
        bowl2.addTopping(eelSauce);
        bowl2.addTopping(sesameOil);
        bowl2.addTopping(sriracha);

        order.addItem(bowl1);
        order.addItem(bowl2);
        order.addItem(coke);
        order.addItem(edamame);
        order.addItem(takoyaki);

        System.out.printf("Subtotal: %.2f\n", order.getSubtotal());
        System.out.printf("Tax: %.2f\n", order.getTax());
        System.out.printf("Total: %.2f", order.getTotal());
        System.out.println("\n");

        ReceiptFileManager.previewReceipt(order);
        ReceiptFileManager.saveReceipt(order);
    }
}
