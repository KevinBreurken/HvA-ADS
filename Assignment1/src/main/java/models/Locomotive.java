package models;

/**
 * A Locomotive is the very first part that makes up a Train.
 * Can be in relation with zero or one Train.
 * @author HvA HBO-ICT, Irene Doodeman, Kevin Breurken
 */
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

    @Override
    public String toString() {
        return String.format("[Loc-%d]",locNumber);
    }

}
