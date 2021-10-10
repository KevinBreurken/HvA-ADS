package models;

public class Product {
    private final long barcode;
    private String title;
    private double price;

    public Product(long barcode) {
        this.barcode = barcode;
    }

    public Product(long barcode, String title, double price) {
        this(barcode);
        this.title = title;
        this.price = price;
    }

    /**
     * parses product information from a textLine with format: barcode, title, price
     *
     * @param textLine
     * @return a new Product instance with the provided information
     * or null if the textLine is corrupt or incomplete
     */
    public static Product fromLine(String textLine) {
        Product newProduct = null;

        String[] splittedLine = textLine.split(",");
        if(splittedLine.length != 3)
            System.err.printf("textLine [%s] is corrupted or incomplete",textLine);
        for (int i = 0; i < splittedLine.length; i++) {
            System.out.println(splittedLine[i]);
        }

        return newProduct;
    }

    public long getBarcode() {
        return barcode;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Product)) return false;
        return this.getBarcode() == ((Product) other).getBarcode();
    }

    // TODO add public and private methods as per your requirements

    @Override
    public String toString() {
        return String.format("%d/%s", getBarcode(), getTitle());
    }
}
