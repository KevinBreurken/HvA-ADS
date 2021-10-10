package models;

import java.util.List;

public class Purchase {
    private final Product product;
    private int count;

    public Purchase(Product product, int count) {
        this.product = product;
        this.count = count;
    }

    /**
     * parses purchase summary information from a textLine with format: barcode, amount
     *
     * @param textLine
     * @param products a list of products ordered and searchable by barcode
     *                 (i.e. the comparator of the ordered list shall consider only the barcode when comparing products)
     * @return a new Purchase instance with the provided information
     * or null if the textLine is corrupt or incomplete
     */
    public static Purchase fromLine(String textLine, List<Product> products) {
        Purchase newPurchase = null;

        // TODO convert the information in the textLine to a new Purchase instance
        //  use the products.indexOf to find the product that is associated with the barcode of the purchase
        String[] splittedLine = textLine.split(",");

        if (splittedLine.length != 2) {
            System.err.printf("textLine [%s] is corrupted or incomplete for a Product", textLine);
            return null;
        }

        //TODO: Add the found product with the indexof method.
        newPurchase = new Purchase(null, Integer.parseInt(splittedLine[1]));

        return newPurchase;
    }

    /**
     * add a delta amount to the count of the purchase summary instance
     *
     * @param delta
     */
    public void addCount(int delta) {
        this.count += delta;
    }

    public long getBarcode() {
        return this.product.getBarcode();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    // TODO add public and private methods as per your requirements

    @Override
    public String toString() {
        double salesAmount = getProduct().getPrice() * getCount();

        return String.format("%s/%d/%.2f", getProduct(), getCount(), salesAmount);
    }
}
