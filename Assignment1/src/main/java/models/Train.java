package models;

/**
 * Functions as singly-ended, doubly linked list for Wagons with a Locomotive object.
 * A (sequence of) Wagon(s) can be attached to the Locomotive of this train.
 * Always has a Locomotive and can have none or more Wagons.
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
     * @return the number of Wagons connected to the train
     */
    public int getNumberOfWagons() {
        if (!hasWagons()) return 0;

        Wagon lastWagon = getFirstWagon();
        int numberOfWagons = 1;

        while (lastWagon.hasNextWagon()) {
            lastWagon = lastWagon.getNextWagon();
            numberOfWagons++;
        }

        return numberOfWagons;
    }

    /**
     * @return the last wagon attached to the train
     */
    public Wagon getLastWagonAttached() {
        Wagon lastWagon = getFirstWagon();

        if (hasWagons()) {
            while (lastWagon.hasNextWagon()) {
                lastWagon = lastWagon.getNextWagon();
            }
        }

        return lastWagon;
    }

    /**
     * @return the total number of seats on a passenger train
     * (return 0 for a freight train)
     */
    public int getTotalNumberOfSeats() {
        if (!isPassengerTrain()) return 0;

        int numberOfSeats = 0;

        if (hasWagons()) {
            PassengerWagon lastWagon = (PassengerWagon) firstWagon;
            while (lastWagon.hasNextWagon()) {
                numberOfSeats += lastWagon.getNumberOfSeats();
                lastWagon = (PassengerWagon) lastWagon.getNextWagon();
            }

            numberOfSeats += lastWagon.getNumberOfSeats();
        }

        return numberOfSeats;
    }

    /**
     * calculates the total maximum weight of a freight train
     *
     * @return the total maximum weight of a freight train
     * (return 0 for a passenger train)
     */
    public int getTotalMaxWeight() {
        //Check if this train has freight wagons.
        if (!isFreightTrain()) return 0;

        int totalMaxWeight = 0;

        if (hasWagons()) {
            FreightWagon lastWagon = (FreightWagon) firstWagon;
            while (lastWagon.hasNextWagon()) {
                totalMaxWeight += lastWagon.getMaxWeight();
                lastWagon = (FreightWagon) lastWagon.getNextWagon();
            }
            totalMaxWeight += lastWagon.getMaxWeight();
        }

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
        if (getEngine().getMaxWagons() < (getNumberOfWagons() + wagon.getTailLength() + 1))
            return false;

        return true;
    }

    /**
     * Checks if the given Wagon is compatible with the current Train.
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
        if (findWagonById(wagon.id) != null)
            return false;

        if (!canAttach(wagon)) return false;

        if (!hasWagons()) {
            setFirstWagon(wagon);
            return true;
        }

        Wagon lastWagonOfNewGroup = wagon.getLastWagonAttached();
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
            if (position > 1) return false;
            attachToRear(wagon);
            return true;
        }

        if (!canAttach(wagon)) return false;

        int currentLength = getFirstWagon().getTailLength() + 1;
        //Check if the position exist
        if (position <= 0 || position > currentLength) return false;

        //attaches and detaches the wagons
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

        if (wagonToMove == null) return false;
        if (!toTrain.isCompatible(wagonToMove)) return false;
        // Can we fit this wagon on the engine?
        if (toTrain.getEngine().getMaxWagons() < toTrain.getNumberOfWagons() + 1) return false;

        // If we move the first wagon, attach the next wagon to the train.
        if (getFirstWagon() == wagonToMove)
            setFirstWagon(wagonToMove.getNextWagon());

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
        if (!hasWagons()) return false; // Do we have wagons to split?

        Wagon wagonAtPosition = findWagonAtPosition(position);

        if (wagonAtPosition == null) return false; // Does the wagon exist?
        if (!toTrain.canAttach(wagonAtPosition)) return false; // Can we attach these wagons to the toTrain?

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
        if (!hasWagons()) return;

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
        if (!canAttach(wagon)) return false;

        if (getNumberOfWagons() == engine.getMaxWagons()) return false;

        Wagon lastWagon = getLastWagonAttached();

        // The rear is the back of the train.
        if (lastWagon == null) {
            setFirstWagon(wagon);
            return true;
        }

        // Attach wagon to the back.
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
            sb.append(lastWagon.toString());
            lastWagon = lastWagon.getNextWagon();
        }

        sb.append(String.format(" with %d wagons from %s to %s", getNumberOfWagons(), origin, destination));

        return sb.toString();
    }
}
