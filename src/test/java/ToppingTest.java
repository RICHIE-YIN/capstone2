import com.richie.model.PokeBowl;
import com.richie.model.Topping;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToppingTest {
    @Test
    void getPriceShouldReturnPremiumMultiplier() {
        Topping takoyaki = new Topping("Takoyaki", 2.00, true);
        double price = takoyaki.getPrice();
        assertEquals(3, price);
    }

    @Test
    void getRegularNonPremiumPriceShouldNotMultiply() {
        Topping edamame = new Topping("Edamame", 0.50, false);
        double price = edamame.getPrice();
        assertEquals(0.50, price);
    }

    @Test
    void smallBowlShouldBeNineFifty() {
        PokeBowl smallBowl = new PokeBowl("Small bowl", "White Rice", "S");
        double price = smallBowl.getPrice();
        assertEquals(9.50, price);
    }

    @Test
    void mediumBowlWithTwoToppingsShouldReturnCorrectPrice() {
        PokeBowl mediumBowl = new PokeBowl("Medium Test Bowl", "White Rice", "M");
        Topping takoyaki = new Topping("Takoyaki", 2.0, true);
        Topping edamame = new Topping("Edamame", .50, false);
        mediumBowl.addTopping(takoyaki);
        mediumBowl.addTopping(edamame);
        double price = mediumBowl.getPrice();
        //M bowl = 9.50 + takoyaki 3 + lettuce .50 = 13
        assertEquals(15.50, price);
    }

    @Test
    void emptyBowlWithNoToppings() {
        PokeBowl pokeBowl = new PokeBowl("Empty Bowl", "Brown Rice", "L");
        double price = pokeBowl.getPrice();
        //L bowl should = 15.50
        assertEquals(15.50, price);
    }
}