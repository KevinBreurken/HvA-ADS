package models;

/**
 * Child class of Wagon.
 * @author HvA HBO-ICT, Irene Doodeman, Kevin Breurken
 */
public class PassengerWagon extends Wagon {
    private int numberOfSeats;

    public PassengerWagon(int wagonId, int numberOfSeats) {
        super(wagonId);
        this.numberOfSeats = numberOfSeats;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }
}
