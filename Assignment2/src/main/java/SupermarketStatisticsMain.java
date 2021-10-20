import models.Product;
import models.Purchase;
import models.PurchaseTracker;

import java.util.ArrayList;
import java.util.Comparator;

public class SupermarketStatisticsMain {

    public static void main(String[] args) {
        System.out.println("Welcome to the HvA Supermarket Statistics processor\n");

        PurchaseTracker purchaseTracker = new PurchaseTracker();

        purchaseTracker.importProductsFromVault("/products.txt");
        purchaseTracker.importPurchasesFromVault("/purchases");

        purchaseTracker.showTops(5, "worst sales volume",
                Comparator.comparingInt(Purchase::getCount)
        );

        purchaseTracker.showTops(5, "best sales revenue",
                Comparator.comparingDouble(Purchase::getSalesAmount).reversed()
        );

        purchaseTracker.showTotals();

//        Purchase.fromLine("8712100516381, 19", purchaseTracker);
    }

}
