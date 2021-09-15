package models;

public class Train {
    private String origin;
    private String destination;
    private Locomotive engine;
    private Wagon firstWagon;

    /* Representation invariants:
        firstWagon == null || firstWagon.previousWagon == null
        engine != null
     */

    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    /* three helper methods that are usefull in other methods */
    public boolean hasWagons() {
        return firstWagon != null;
    }

    public boolean isPassengerTrain() {
        return firstWagon instanceof PassengerWagon || hasWagons();
    }

    public boolean isFreightTrain() {
        return firstWagon instanceof FreightWagon || hasWagons();
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
        if (firstWagon == null) return 0;

        int numberOfWagons = 1;
        Wagon lastWagon = firstWagon;

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
        Wagon lastWagon = firstWagon;

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
        if (isFreightTrain()) return 0;

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
        if (isPassengerTrain()) return 0;

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
    public Wagon findWagonAtPosition(int position) { //4
        if (!hasWagons()) return null;

        Wagon lastWagon = firstWagon;
        int currentPosition = 1;

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

        Wagon currentWagon = firstWagon;

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
        if ((isFreightTrain() && !(wagon instanceof FreightWagon))
                || (isPassengerTrain() && !(wagon instanceof PassengerWagon)))
            return false;

        if (getEngine().getMaxWagons() < (getNumberOfWagons() + wagon.getTailLength() + 1))
            return false;

        return true;
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
        if (!canAttach(wagon)) return false;

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
        if (!canAttach(wagon)) return false;

        //Check if the position exists
        int currentLength = getFirstWagon().getTailLength() + 1;
        if (position <= 0 || position > currentLength) return false;

        //Attach 3c - 1B
        Wagon lastWagonOfNewGroup = wagon.getLastWagonAttached();
        Wagon firstOfOriginalTail = findWagonAtPosition(position);
        lastWagonOfNewGroup.setNextWagon(firstOfOriginalTail);
        firstOfOriginalTail.setPreviousWagon((lastWagonOfNewGroup));
        //Attach 3c - 1B

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
        // TODO

        return false;
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

        //returns false if the trains are not compatible
//        if ((isFreightTrain() && !(toTrain.isFreightTrain())) || (isPassengerTrain() && !(toTrain.isPassengerTrain())))
//            return false;

        if (!hasWagons()) return false;

        if (toTrain.canAttach(getFirstWagon())) {

        }

        Wagon wagon = findWagonAtPosition(position);
        //returns false if the position is not valid
        if (wagon == null)
            return false;


        //splits the train and moves the cut-off trains to the new train
        toTrain.attachToRear(wagon);
        //todo prev/next

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
        // TODO

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
        if (getNumberOfWagons() == engine.getMaxWagons()) return false;

        Wagon lastWagon = getLastWagonAttached();

        if (lastWagon == null) {
            setFirstWagon(wagon);
        } else {
            wagon.setPreviousWagon(lastWagon);
            lastWagon.setNextWagon(wagon);
        }

        return true;
    }

    public boolean attachToRear(PassengerWagon wagon) {
        if (firstWagon == null || firstWagon instanceof PassengerWagon) {
            attachToRear((Wagon) wagon);
            return true;
        }
        return false;
    }

    public boolean attachToRear(FreightWagon wagon) {
        if (firstWagon == null || firstWagon instanceof FreightWagon) {
            attachToRear((Wagon) wagon);
            return true;
        }
        return false;
    }

    public boolean insertAtFront(FreightWagon wagon) {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Train: %s, %s\n", origin, destination));
        sb.append("Wagons:\n");

        Wagon lastWagon = firstWagon;
        while (lastWagon != null && lastWagon.getNextWagon() != null) {
            sb.append(lastWagon.toString());
            lastWagon = lastWagon.getNextWagon();
        }
        sb.append(lastWagon.toString());

        return sb.toString();
    }
}
