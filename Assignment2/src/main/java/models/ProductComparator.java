package models;

import java.util.Comparator;

public class ProductComparator implements Comparator<Product> {

    @Override
    public int compare(Product o1, Product o2) {
        return Long.compare(o1.getBarcode(),o2.getBarcode());
    }

    public int compareByBarcode(long o1, long o2) {
        return Long.compare(o1,o2);
    }
}
