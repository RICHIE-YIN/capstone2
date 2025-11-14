import com.richie.model.Sides;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SidesTest {

    @Test
    void allSidesShouldBeTwoFifty() {
        Sides misoSoup = new Sides("Miso Soup");
        Sides gyoza = new Sides("Gyoza");

        assertEquals(2.50, misoSoup.getPrice());
        assertEquals(2.50, gyoza.getPrice());
    }

    @Test
    void sideNameAndTypeShouldMatch() {
        Sides edamame = new Sides("Edamame");

        assertEquals("Edamame", edamame.getType());
        assertEquals("Edamame", edamame.getName());
    }
}
