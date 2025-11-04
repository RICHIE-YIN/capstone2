import com.richie.model.Sandwich;
import com.richie.model.Topping;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToppingTest {
    @Test
    void getPriceShouldReturnPremiumMultiplier() {
        Topping bacon = new Topping("Bacon", 1.00, true);
        double price = bacon.getPrice();
        assertEquals(1.50, price);
    }

    @Test
    void getRegularNonPremiumPriceShouldNotMultiply() {
        Topping lettuce = new Topping("Lettuce", 0.50, false);
        double price = lettuce.getPrice();
        assertEquals(0.50, price);
    }

    @Test
    void fourInchSandwichShouldBeFiveFifty() {
        Sandwich fourInSandwich = new Sandwich("Four In Sandwich", "Wheat", "4");
        double price = fourInSandwich.getPrice();
        assertEquals(5.50, price);
    }

    @Test
    void eightInSandwichWithTwoToppingsShouldReturnCorrectPrice() {
        Sandwich eightInSandwich = new Sandwich("Eight In Sandwich", "White", "8");
        Topping bacon = new Topping("Bacon", 1.0, true);
        Topping lettuce = new Topping("Lettuce", .50, false);
        eightInSandwich.addTopping(bacon);
        eightInSandwich.addTopping(lettuce);
        double price = eightInSandwich.getPrice();
        //8in sandwich = 7 + bacon 1.50 + lettuce .50 = 9
        assertEquals(9, price);
    }

    @Test
    void emptySandwichWithNoToppings() {
        Sandwich sandwich = new Sandwich("Super Sour Sandwich", "Sourdough", "12");
        double price = sandwich.getPrice();
        //12 in sandwich should equal 8.50
        assertEquals(8.50, price);
    }
}
