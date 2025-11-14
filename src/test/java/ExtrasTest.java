import com.richie.model.Extra;
import com.richie.model.Topping;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtrasTest {

    @Test
    void premiumToppingShouldHaveTwoDollarUpcharge() {
        Topping salmon = new Topping("Salmon", 2.00, true);
        Extra extraSalmon = new Extra(salmon);

        double price = extraSalmon.getPrice();

        assertEquals(2.00, price);
    }

    @Test
    void regularToppingShouldHaveFiftyCentUpcharge() {
        Topping cucumber = new Topping("Cucumber", 0.25, false);
        Extra extraCucumber = new Extra(cucumber);

        double price = extraCucumber.getPrice();

        assertEquals(0.50, price);
    }

    @Test
    void extraNameShouldBePrefixedWithExtra() {
        Topping avocado = new Topping("Avocado", 1.50, true);
        Extra extraAvocado = new Extra(avocado);

        String name = extraAvocado.getName();

        assertEquals("Extra Avocado", name);
    }
}
