package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;

public class PurchaseTracker {
    private final String PURCHASE_FILE_PATTERN = ".*\\.txt";

    private OrderedList<Product> products;        // the reference list of all Products available from the SuperMarket chain
    private OrderedList<Purchase> purchases;      // the aggregated volumes of all purchases of all products across all branches

    public PurchaseTracker() {
        products = new OrderedArrayList<>(Comparator.comparing(Product::getBarcode));
        purchases = new OrderedArrayList<>(Comparator.comparing(Purchase::getBarcode));
    }

    /**
     * imports a collection of items from a text file which provides one line for each item
     *
     * @param items     the list to which imported items shall be added
     * @param filePath  the file path of the source text file
     * @param converter a function that can convert a text line into a new item instance
     * @param <E>       the (generic) type of each item
     */
    public static <E> void importItemsFromFile(List<E> items, String filePath, Function<String, E> converter) {
        Scanner scanner = createFileScanner(filePath);

        //For every line in text file...
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            //Convert line to new object of type E.
            E item = converter.apply(line);
            items.add(item);
        }
    }

    /**
     * helper method to create a scanner on a file an handle the exception
     *
     * @param filePath
     * @return
     */
    private static Scanner createFileScanner(String filePath) {
        try {
            return new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFound exception on path: " + filePath);
        }
    }

    /**
     * imports all products from a resource file that is common to all branches of the Supermarket chain
     *
     * @param resourceName
     */
    public void importProductsFromVault(String resourceName) {
        this.products.clear();

        // load all products from the text file
        importItemsFromFile(this.products,
                PurchaseTracker.class.getResource(resourceName).getPath(),
                Product::fromLine);

        // sort the products for efficient later retrieval
        this.products.sort();

        System.out.printf("Imported %d products from %s.\n", products.size(), resourceName);
    }

    /**
     * imports and merges all raw purchase data of all branches from the hierarchical file structure of the vault
     *
     * @param resourceName
     */
    public void importPurchasesFromVault(String resourceName) {
        this.purchases.clear();

        mergePurchasesFromFileRecursively(
                PurchaseTracker.class.getResource(resourceName).getPath());

        System.out.printf("Accumulated purchases of %d products from files in %s.\n", this.purchases.size(), resourceName);
    }

    /**
     * traverses the purchases vault recursively and processes every data file that it finds
     *
     * @param filePath
     */
    private void mergePurchasesFromFileRecursively(String filePath) {

        File file = new File(filePath);

        if (file.isDirectory()) {
            // the file is a folder (a.k.a. directory)
            //  retrieve a list of all files and sub folders in this directory
            File[] filesInDirectory = Objects.requireNonNullElse(file.listFiles(), new File[0]);

            for (File value : filesInDirectory)
                mergePurchasesFromFileRecursively(value.getPath());

        } else if (file.getName().matches(PURCHASE_FILE_PATTERN)) {
            // the file is a regular file that matches the target pattern for raw purchase files
            // merge the content of this file into this.purchases
            this.mergePurchasesFromFile(file.getAbsolutePath());
        }

    }

    /**
     * show the top n purchases according to the ranking criterium specified by ranker
     *
     * @param n        the number of top purchases to be shown
     * @param subTitle some title text that clarifies the list
     * @param ranker   the comparator used to rank the purchases
     */
    public void showTops(int n, String subTitle, Comparator<Purchase> ranker) {
        System.out.printf("%d purchases with %s:\n", n, subTitle);
        // helper list to rank the purchases without disturbing the ordening of the original list
        OrderedList<Purchase> tops = new OrderedArrayList<>(ranker);

        // add all purchases to the new tops list, and sort the list
        tops.addAll(this.purchases);
        tops.sort();

        // show the top items
        for (int rank = 0; rank < n && rank < tops.size(); rank++) {
            System.out.printf("%d: %s\n", rank + 1, tops.get(rank));
        }
    }

    /**
     * shows total volume and total revenue sales statistics
     */
    public void showTotals() {
        System.out.printf("Total volume of all purchases: %.0f\n",
                purchases.aggregate(Purchase::getCount));

        System.out.printf("Total revenue from all purchases: %.2f\n",
                purchases.aggregate(Purchase::getSalesAmount));
    }

    /**
     * imports another batch of raw purchase data from the filePath text file
     * and merges the purchase amounts with the earlier imported and accumulated collection in this.purchases
     *
     * @param filePath
     */
    private void mergePurchasesFromFile(String filePath) {
        // create a temporary ordered list for the additional purchases, ordered by same comparator as the main list
        OrderedList<Purchase> newPurchases = new OrderedArrayList<>(this.purchases.getOrdening());

        // re-sort the accumulated purchases for efficient searching
        this.purchases.sort();

        importItemsFromFile(newPurchases, filePath, s -> Purchase.fromLine(s,products));

        // merge each new purchase into the purchases list.
        for (Purchase purchase : newPurchases)
            this.purchases.merge(purchase,(p1,p2) -> { p1.addCount(p2.getCount()); return p1;});

    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }
}
