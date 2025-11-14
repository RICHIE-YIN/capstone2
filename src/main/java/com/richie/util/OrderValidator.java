package com.richie.util;

import com.richie.model.*;

public class OrderValidator {

    private OrderValidator() {
    }

    // returns true if the order passes all business rules
    public static boolean isValid(Order order) {

        // order cannot be empty
        if (order.getItems().isEmpty()) {
            return false;
        }

        int bowlCount = 0;
        int drinkCount = 0;
        int sideCount = 0;

        for (Product item : order.getItems()) {
            if (item instanceof PokeBowl || item instanceof SignaturePokeBowl) {
                bowlCount++;
            } else if (item instanceof Drink) {
                drinkCount++;
            } else if (item instanceof Sides) {
                sideCount++;
            }
        }

        // if no entree, must have at least one drink or one side
        if (bowlCount == 0 && drinkCount == 0 && sideCount == 0) {
            return false;
        }

        return true;
    }

}
