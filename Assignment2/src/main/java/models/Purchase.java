package models;

import java.util.List;

public class Purchase {

    private static final String DELIMITER = ", ";
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
        String[] splittedLine = textLine.split(", ");
        //Putting the data in variables for the sake of readability.
        long barcode = Long.parseLong(splittedLine[0]);
        int count = Integer.parseInt(splittedLine[1]);

        //Returns if the given String isn't valid.
        if (splittedLine.length != 2) {
            System.err.printf("textLine [%s] is corrupted or incomplete for a Product", textLine);
            return null;
        }

        int index = products.indexOf(new Product(barcode));
        if(index <= -1){
            System.err.printf("textLine [%s] is corrupted or incomplete for a Product", textLine);
            return null;
        }
        return new Purchase(products.get(index), count);
//        System.out.println("INDEX OF PRODUCT: " + index);
        //TODO ask about implementation suggestion
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

    private double getSalesAmount() {
        return product.getPrice() * count;
    }

    // TODO add public and private methods as per your requirements

    @Override
    public String toString() {
        if (product != null) {
            double salesAmount = getSalesAmount();
            return String.format("%d/%s/%d/%.2f", product.getBarcode(),product.getTitle(), count, salesAmount);
        } else return "The product is corrupted or incomplete";
    }
}
