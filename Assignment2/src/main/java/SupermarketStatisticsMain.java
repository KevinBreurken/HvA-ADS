import models.Product;
import models.Purchase;
import models.PurchaseTracker;

import java.util.ArrayList;

public class SupermarketStatisticsMain {

    public static void main(String[] args) {
        System.out.println("Welcome to the HvA Supermarket Statistics processor\n");
//        testsForAssignmentSteps();

        PurchaseTracker purchaseTracker = new PurchaseTracker();

        purchaseTracker.importProductsFromVault("/products.txt");
        purchaseTracker.importPurchasesFromVault("/purchases");
//
//        // TODO provide the comparators that can order the purchases by specified criteria
//        purchaseTracker.showTops(5, "worst sales volume",
//                null
//        );
//        purchaseTracker.showTops(5, "best sales revenue",
//                null
//        );
//
//        purchaseTracker.showTotals();
    }

    public static void testsForAssignmentSteps() {

        System.out.println("1. Complete the implementations of the Product and Purchase classes:");
        System.out.println("\ta. Product and Purchase toString methods");
        Product testProduct = new Product(1234567, "Test product", 5.99);
        Purchase testPurchase = new Purchase(testProduct, 15);
        System.out.printf("Printing product:\n%s \n", testProduct);
        System.out.printf("Printing purchase:\n%s \n\n", testPurchase);

        System.out.println("\tb. fromLine method");
        System.out.println("Create a product and Purchase with the fromLine methods");
        testProduct = Product.fromLine("8712100516381, Coca Cola regular 1L, 1.02");
        ArrayList<Product> productList = new ArrayList<>();
        productList.add(testProduct);
        testPurchase = Purchase.fromLine("8712100516381, 19",productList);
        System.out.printf("Printing product:\n%s \n", testProduct);
        System.out.printf("Printing purchase:\n%s \n\n", testPurchase);
    }

}
