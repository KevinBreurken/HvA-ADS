package models;

public class Locomotive {
    private int locNumber;
    private int maxWagons;


    public Locomotive(int locNumber, int maxWagons) {
        this.locNumber = locNumber;
        this.maxWagons = maxWagons;
    }

    public int getMaxWagons() {
        return maxWagons;
    }


    // TODO toString
    @Override
    public String toString() {
        return "Locomotive{" +
                "locNumber=" + locNumber +
                ", maxWagons=" + maxWagons +
                '}';
    }

}
