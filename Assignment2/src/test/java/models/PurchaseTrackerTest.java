package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseTrackerTest {

    PurchaseTracker purchaseTracker;

    @BeforeEach
    private void setup() {
        purchaseTracker = new PurchaseTracker();

        purchaseTracker.importProductsFromVault("/products.txt");

        purchaseTracker.importPurchasesFromVault("/purchases");
    }

    @Test
    public void importVaultCheck() {
        assertEquals(61, purchaseTracker.getProducts().size());
        assertEquals(61, purchaseTracker.getPurchases().size());
        assertEquals(16730, purchaseTracker.getPurchases().stream().mapToInt(Purchase::getCount).sum());
    }
}
