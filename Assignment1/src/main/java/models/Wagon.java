package models;

import java.util.Objects;

/**
 * Parent class for Freight- and PassengerWagons.
 * A Wagon can be attached to a sequence of other wagons and an Engine.
 * Can be part of a train.
 */
public abstract class Wagon {
    protected int id;               // some unique ID of a Wagon
    private Wagon nextWagon;        // another wagon that is appended at the tail of this wagon
    // a.k.a. the successor of this wagon in a sequence
    // set to null if no successor is connected
    private Wagon previousWagon;    // another wagon that is prepended at the front of this wagon
    // a.k.a. the predecessor of this wagon in a sequence
    // set to null if no predecessor is connected


    // representation invariant propositions:
    // tail-connection-invariant:   wagon.nextWagon == null or wagon == wagon.nextWagon.previousWagon
    // front-connection-invariant:  wagon.previousWagon == null or wagon = wagon.previousWagon.nextWagon

    public Wagon(int wagonId) {
        this.id = wagonId;
    }

    public int getId() {
        return id;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public void setNextWagon(Wagon nextWagon) {
        this.nextWagon = nextWagon;
    }

    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    public void setPreviousWagon(Wagon previousWagon) {
        this.previousWagon = previousWagon;
    }

    /**
     * @return whether this wagon has a wagon appended at the tail
     */
    public boolean hasNextWagon() {
        return nextWagon != null;
    }

    /**
     * @return whether this wagon has a wagon prepended at the front
     */
    public boolean hasPreviousWagon() {
        return previousWagon != null;
    }

    /**
     * Returns the last wagon attached to it, if there are no wagons attached to it then this wagon is the last wagon.
     *
     * @return the wagon
     */
    public Wagon getLastWagonAttached() {
        Wagon lastwagon = this;
        while (lastwagon.hasNextWagon()) {
            lastwagon = lastwagon.getNextWagon();
        }
        return lastwagon;
    }

    /**
     * @return the length of the tail of wagons towards the end of the sequence
     * excluding this wagon itself.
     */
    public int getTailLength() {
        int currentAmount = 0;
        //Iterate until we are at the end of the wagons.
        Wagon lastwagon = this;
        while (lastwagon.hasNextWagon()) {
            lastwagon = lastwagon.getNextWagon();
            currentAmount++;
        }

        return currentAmount;
    }

    /**
     * Attaches the tail wagon behind this wagon, if and only if this wagon has no wagon attached at its tail
     * and if the tail wagon has no wagon attached in front of it.
     *
     * @param tail the wagon to attach behind this wagon.
     * @throws IllegalStateException if this wagon already has a wagon appended to it.
     * @throws IllegalStateException if tail is already attached to a wagon in front of it.
     */
    public void attachTail(Wagon tail) {
        //checks if the wagons can be attached
        if (hasNextWagon()) throw new IllegalStateException(this + " : " + tail + " wagon already has a next wagon!");
        if (tail.hasPreviousWagon())
            throw new IllegalStateException(this + " : " + tail + "tail wagon already has a previous wagon!");

        //Attaches the tail wagon to this wagon (sustaining the invariant propositions).
        tail.reAttachTo(this);
    }

    /**
     * Detaches the tail from this wagon and returns the first wagon of this tail.
     *
     * @return the first wagon of the tail that has been detached
     * or <code>null</code> if it had no wagons attached to its tail.
     */
    public Wagon detachTail() {
        if (hasNextWagon()) {
            Wagon tailWagon = getNextWagon();
            tailWagon.detachFront();
            setNextWagon(null);
            return tailWagon;
        }

        return null;
    }

    /**
     * Detaches this wagon from the wagon in front of it.
     * No action if this wagon has no previous wagon attached.
     *
     * @return the former previousWagon that has been detached from,
     * or <code>null</code> if it had no previousWagon.
     */
    public Wagon detachFront() {
        Wagon previousWagon = getPreviousWagon();

        if (hasPreviousWagon()) {
            setPreviousWagon(null);
            previousWagon.detachTail();
            return previousWagon;
        }

        return null;
    }

    /**
     * Replaces the tail of the <code>front</code> wagon by this wagon
     * Before such reconfiguration can be made,
     * the method first disconnects this wagon form its predecessor,
     * and the <code>front</code> wagon from its current tail.
     *
     * @param front the wagon to which this wagon must be attached to.
     */
    public void reAttachTo(Wagon front) {
        //Check if the wagon already exists in the wagon we want to attach to.
        Wagon lastwagon = this; //b
        while (lastwagon.hasNextWagon()) {
            if (lastwagon.toString().equals(front.toString()))
                System.err.println("Can't attach wagon to child wagon.");
            lastwagon = lastwagon.getNextWagon();
        }

        //Detaches any existing connections that will be rearranged
        if (hasPreviousWagon()) {
            previousWagon.setNextWagon(null);
            setPreviousWagon(null);
        }

        //Attaches this wagon to its new predecessor front (sustaining the invariant propositions).
        front.setNextWagon(this);
        setPreviousWagon(front);
    }

    /*
     * Removes this wagon from the sequence that it is part of,
     * and reconnects its tail to the wagon in front of it, if it exists.
     */
    public void removeFromSequence() {
        Wagon wagonPrevious = getPreviousWagon();
        Wagon wagonNext = getNextWagon();

        //removes the Wagon from the sequence
        detachFront();
        detachTail();

        //attaches the previous and next from the removed object to each other if possible
        if (wagonPrevious != null && wagonNext != null)
            wagonNext.reAttachTo(wagonPrevious);

    }


    /**
     * Reverses the order in the sequence of wagons from this Wagon until its final successor.
     * The reversed sequence is attached again to the wagon in front of this Wagon, if any.
     * No action if this Wagon has no succeeding next wagon attached.
     *
     * @return the new start Wagon of the reversed sequence (with is the former last Wagon of the original sequence)
     */
    public Wagon reverseSequence() {
        Wagon previousWagonOnStart = detachFront();
        Wagon lastBeforeReverse = getLastWagonAttached(); // The last wagon will be the first wagon at the end.

        Wagon anchorWagon = this;
        Wagon lastPutToFront = null; // Keep a record of the last wagon we put to the front

        while (anchorWagon.hasNextWagon()) {
            Wagon wagonToPutToFront = anchorWagon.getNextWagon();
            wagonToPutToFront.removeFromSequence();

            wagonToPutToFront.attachTail(Objects.requireNonNullElse(lastPutToFront, anchorWagon));

            lastPutToFront = wagonToPutToFront;
        }

        // Attach it back to the start previous wagon if it exists
        if (previousWagonOnStart != null)
            lastPutToFront.reAttachTo(previousWagonOnStart);

        return lastBeforeReverse;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[Wagon-%d]", getId()));
        if (nextWagon != null)
            sb.append(nextWagon);

        return sb.toString();
    }

}
