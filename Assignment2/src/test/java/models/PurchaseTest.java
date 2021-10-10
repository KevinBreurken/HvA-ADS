package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseTest {

    Product stroopwafels, marsbar, snickers;
    List<Product> products = new ArrayList<>();

    @BeforeEach
    private void setup() {
        stroopwafels = new Product(111111111111111L, "Stroopwafels 10st", 1.23);
        marsbar = new Product(222222222222222L, "Mars bar", 0.86);
        snickers = new Product(333333333333333L, "Snickers", 0.97);
        products = List.of(stroopwafels,marsbar,snickers);
    }



    @Test
    public void canConvertATextLineToAPurchase() {
        Purchase purchase1 = Purchase.fromLine("111111111111111, 10", products);
        Purchase purchase2 = Purchase.fromLine("222222222222222, 20", products);
        Purchase purchase3 = Purchase.fromLine("333333333333333, 30", products);

        assertEquals(10, purchase1.getCount());
        assertEquals(20, purchase2.getCount());
        assertSame(stroopwafels, purchase1.getProduct());
        assertSame(marsbar, purchase2.getProduct());
        assertSame(snickers, purchase3.getProduct());
    }

    @Test
    public void aPurchaseHasAStringRepresentation() {
        Purchase purchase1 = Purchase.fromLine("111111111111111, 10", products);
        assertEquals("111111111111111/Stroopwafels 10st/10/12.30", purchase1.toString());
    }
}