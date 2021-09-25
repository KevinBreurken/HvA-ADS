package models;

/**
 * Functions as singly-ended, doubly linked list for Wagons.
 * Has a Locomotive.
 * A (sequence of) Wagon(s) can be attached to the Locomotive of this train.
 * Always has a Locomotive and can have none or more Wagons.
 *
 * @author HvA HBO-ICT, Irene Doodeman, Kevin Breurken
 */
public class Train {
    private String origin;
    private String destination;
    private Locomotive engine;
    private Wagon firstWagon;

    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    public boolean hasWagons() {
        return firstWagon != null;
    }

    public boolean isPassengerTrain() {
        return firstWagon instanceof PassengerWagon;
    }

    public boolean isFreightTrain() {
        return firstWagon instanceof FreightWagon;
    }

    public Locomotive getEngine() {
        return engine;
    }

    public Wagon getFirstWagon() {
        return firstWagon;
    }

    /**
     * Replaces the current sequence of wagons (if any) in the train
     * by the given new sequence of wagons (if any)
     * (sustaining all representation invariants)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     */
    public void setFirstWagon(Wagon wagon) {
        firstWagon = wagon;
    }

    /**
     * Loops through the sequence of Wagons attached to the train until all have been counted.
     *
     * @return the number of Wagons connected to the train
     */
    public int getNumberOfWagons() {
        if (!hasWagons()) return 0;

        Wagon lastWagon = getFirstWagon();
        int numberOfWagons = 1;  // Set to one since the first wagon is already the last wagon counted.

        while (lastWagon.hasNextWagon()) {
            lastWagon = lastWagon.getNextWagon();
            numberOfWagons++; // The current lastWagon gets counted.
        }

        return numberOfWagons;
    }

    /**
     * Loops through the sequences of Wagons attached to the train
     * until the last wagon in the sequence has been found.
     *
     * @return the last wagon attached to the train
     */
    public Wagon getLastWagonAttached() {
        if (!hasWagons()) return null;

        Wagon lastWagon = getFirstWagon();
        while (lastWagon.hasNextWagon()) {
            lastWagon = lastWagon.getNextWagon();
        }

        return lastWagon;
    }

    /**
     * Loops through the sequence of passengerWagons and add up the amount of seats.
     *
     * @return the total number of seats on a passenger train
     * (return 0 for a freight train)
     */
    public int getTotalNumberOfSeats() {
        // Returns if the Train isn't a passengerWagon or if it doesn't have any Wagons.
        if (!isPassengerTrain() || !hasWagons()) return 0;

        int numberOfSeats = 0;

        PassengerWagon lastWagon = (PassengerWagon) firstWagon;
        while (lastWagon.hasNextWagon()) {
            numberOfSeats += lastWagon.getNumberOfSeats();
            lastWagon = (PassengerWagon) lastWagon.getNextWagon();
        }

        numberOfSeats += lastWagon.getNumberOfSeats(); // Counts the seats of the last wagon attached

        return numberOfSeats;
    }

    /**
     * calculates the total maximum weight of a freight train.
     *
     * @return the total maximum weight of a freight train
     * (return 0 for a passenger train)
     */
    public int getTotalMaxWeight() {
        // Returns if the Train isn't a Freightwagon or if it doesn't have any Wagons.
        if (!isFreightTrain() || !hasWagons()) return 0;

        int totalMaxWeight = 0;

        FreightWagon lastWagon = (FreightWagon) firstWagon;
        while (lastWagon.hasNextWagon()) {
            totalMaxWeight += lastWagon.getMaxWeight();
            lastWagon = (FreightWagon) lastWagon.getNextWagon();
        }

        totalMaxWeight += lastWagon.getMaxWeight(); // Counts the weight of the last wagon attached

        return totalMaxWeight;
    }

    /**
     * Finds the wagon at the given position (starting at 1 for the first wagon of the train)
     *
     * @param position
     * @return the wagon found at the given position
     * (return null if the position is not valid for this train)
     */
    public Wagon findWagonAtPosition(int position) {
        if (!hasWagons()) return null;

        Wagon lastWagon = getFirstWagon();
        int currentPosition = 1;

        if (currentPosition == position) return lastWagon;

        while (lastWagon.hasNextWagon()) {
            lastWagon = lastWagon.getNextWagon();
            currentPosition++;

            if (currentPosition == position) break;
        }

        // Returns the requested Wagon or null if the position isn't valid
        return currentPosition == position ? lastWagon : null;
    }

    /**
     * Finds the wagon with a given wagonId
     *
     * @param wagonId
     * @return the wagon found
     * (return null if no wagon was found with the given wagonId)
     */
    public Wagon findWagonById(int wagonId) {
        if (!hasWagons()) return null;

        Wagon currentWagon = getFirstWagon();

        do {
            if (currentWagon.getId() == wagonId) return currentWagon;
            currentWagon = currentWagon.getNextWagon();
        } while (currentWagon != null);

        // Returns null in the case the id hasn't been found and therefore isn't valid
        return null;
    }

    /**
     * Determines if the given sequence of wagons can be attached to the train
     * Verfies of the type of wagons match the type of train (Passenger or Freight)
     * Verfies that the capacity of the engine is sufficient to pull the additional wagons
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return
     */
    public boolean canAttach(Wagon wagon) {
        //Check if the wagon is of the same type.
        if (!isCompatible(wagon)) return false;

        //Check if the new group of wagons can fit the maximum amount.
        return getEngine().getMaxWagons() >= (getNumberOfWagons() + wagon.getTailLength() + 1);
    }

    /**
     * Checks if the given Wagon is compatible with the current Train.
     *
     * @param wagon
     * @return
     */
    private boolean isCompatible(Wagon wagon) {
        return (!isFreightTrain() || wagon instanceof FreightWagon)
                && (!isPassengerTrain() || wagon instanceof PassengerWagon);
    }

    /**
     * Tries to insert the given sequence of wagons at the front of the train
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the insertion could be completed successfully
     */
    public boolean insertAtFront(Wagon wagon) {
        if (findWagonById(wagon.id) != null) return false;

        if (!canAttach(wagon)) return false;

        // In the case there are no Wagons attached, the Wagon can be set at the first wagon of the sequence.
        if (!hasWagons()) {
            setFirstWagon(wagon);
            return true;
        }

        // Gets the last Wagon attached to the given Wagon.
        Wagon lastWagonOfNewGroup = wagon.getLastWagonAttached();
        // Connects the last Wagon attached to the given Wagon in front of the first wagon connected to the train.
        lastWagonOfNewGroup.setNextWagon(getFirstWagon());
        getFirstWagon().setPreviousWagon(lastWagonOfNewGroup);

        setFirstWagon(wagon);

        return true;
    }

    /**
     * Tries to insert the given sequence of wagons at/before the given wagon position in the train
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible of the engine has insufficient capacity
     * or the given position is not valid in this train)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the insertion could be completed successfully
     */
    public boolean insertAtPosition(int position, Wagon wagon) {
        if (!hasWagons()) {
            if (position > 1) return false; // Returns if the position isn't valid
            attachToRear(wagon);
            return true;
        }

        if (!canAttach(wagon)) return false;

        int currentLength = getFirstWagon().getTailLength() + 1;
        //Check if the position exist
        if (position <= 0 || position > currentLength) return false;

        // Attaches and detaches the wagons
        wagon.getLastWagonAttached().attachTail(findWagonAtPosition(position));

        return true;
    }

    /**
     * Tries to remove one Wagon with the given wagonId from this train
     * and attach it at the rear of the given toTrain
     * No change is made if the removal or attachment cannot be made
     * (when the wagon cannot be found, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     *
     * @param wagonId
     * @param toTrain
     * @return whether the move could be completed successfully
     */
    public boolean moveOneWagon(int wagonId, Train toTrain) {
        Wagon wagonToMove = findWagonById(wagonId);

        // Multiple checks get done before the moving can take place:
        if (wagonToMove == null) return false;
        if (!toTrain.isCompatible(wagonToMove)) return false;
        // Check if the Locomotive of toTrain can hold the Wagon.
        if (toTrain.getEngine().getMaxWagons() < toTrain.getNumberOfWagons() + 1) return false;

        // Sets the next of the first Wagon of this Train
        // in the case that the first Wagon is the Wagon to be moved.
        if (getFirstWagon() == wagonToMove)
            setFirstWagon(wagonToMove.getNextWagon());

        // Removes the Wagon to move and attaches it to toTrain.
        wagonToMove.removeFromSequence();
        toTrain.attachToRear(wagonToMove);

        return true;
    }

    /**
     * Tries to split this train before the given position and move the complete sequence
     * of wagons from the given position to the rear of toTrain.
     * No change is made if the split or re-attachment cannot be made
     * (when the position is not valid for this train, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     *
     * @param position
     * @param toTrain
     * @return whether the move could be completed successfully
     */
    public boolean splitAtPosition(int position, Train toTrain) {
        if (!hasWagons()) return false; // Can't split if there are no Wagons.

        Wagon wagonAtPosition = findWagonAtPosition(position);

        // Checks if the Wagon exists and weather the Wagon can be attached to toTrain
        if (wagonAtPosition == null || !toTrain.canAttach(wagonAtPosition)) return false;

        wagonAtPosition.detachFront();
        // If the position is 1, it needs to detach itself from the train.
        if (position == 1)
            setFirstWagon(null);

        // Attach the wagon to the correct object.
        if (toTrain.hasWagons())
            wagonAtPosition.reAttachTo(toTrain.getLastWagonAttached());
        else toTrain.setFirstWagon(wagonAtPosition);

        return true;
    }

    /**
     * Reverses the sequence of wagons in this train (if any)
     * i.e. the last wagon becomes the first wagon
     * the previous wagon of the last wagon becomes the second wagon
     * etc.
     * (No change if the train has no wagons or only one wagon)
     */
    public void reverse() {
        if (!hasWagons()) return; // Can't reverse if there are no Wagons.

        Wagon wagonToReverse = getFirstWagon();

        wagonToReverse.detachFront();
        setFirstWagon(null);
        attachToRear(wagonToReverse.reverseSequence());
    }

    /**
     * Tries to attach the given sequence of wagons to the rear of the train
     * No change is made if the attachment cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     *
     * @param wagon the first wagon of a sequence of wagons to be attached
     * @return whether the attachment could be completed successfully
     */
    public boolean attachToRear(Wagon wagon) {
        if (!canAttach(wagon) || getNumberOfWagons() == engine.getMaxWagons())
            return false;

        Wagon lastWagon = getLastWagonAttached();

        if (lastWagon == null) {
            setFirstWagon(wagon); // The rear is the front in this case.
            return true;
        }

        // Attaches the Wagon to the back.
        wagon.detachFront();
        lastWagon.attachTail(wagon);

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getEngine().toString());

        // Print each wagon attached to this train.
        Wagon lastWagon = getFirstWagon();
        while (lastWagon != null) {
            sb.append(lastWagon);
            lastWagon = lastWagon.getNextWagon();
        }

        sb.append(String.format(" with %d wagons from %s to %s", getNumberOfWagons(), origin, destination));

        return sb.toString();
    }
}
