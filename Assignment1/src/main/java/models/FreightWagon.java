package models;

/**
 * Child class of Wagon.
 * @author HvA HBO-ICT, Irene Doodeman, Kevin Breurken
 */
public class FreightWagon extends Wagon {
    private int maxWeight;

    public FreightWagon(int wagonId, int maxWeight) {
        super(wagonId);
        this.maxWeight = maxWeight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }
}
