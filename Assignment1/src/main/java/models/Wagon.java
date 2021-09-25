package models;

import java.util.Objects;

/**
 * Parent class for Freight- and PassengerWagons.
 * A Wagon can be attached to a sequence of other wagons (or one) and a Locomotive.
 * Can be part of a train.
 *
 * @author HvA HBO-ICT, Irene Doodeman, Kevin Breurken
 */
public abstract class Wagon {
    protected int id;
    private Wagon nextWagon;
    private Wagon previousWagon;

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
        // Cycle through each nextWagon to find the last.
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
        int amountOfWagons = 0;
        Wagon currentWagon = this;

        while (currentWagon.hasNextWagon()) {
            //Iterate until the end of the tail of this Wagon has been reached.
            currentWagon = currentWagon.getNextWagon();
            amountOfWagons++;
        }

        return amountOfWagons;
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
        // Checks if the wagons can be attached
        if (hasNextWagon())
            throw new IllegalStateException(this + " : " + tail + " wagon already has a next wagon!");
        if (tail.hasPreviousWagon())
            throw new IllegalStateException(this + " : " + tail + "tail wagon already has a previous wagon called " + tail.getPreviousWagon());

        // Attaches the tail wagon to this wagon (sustaining the invariant propositions).
        tail.reAttachTo(this);
    }

    /**
     * Detaches the tail from this wagon and returns the first wagon of this tail.
     *
     * @return the first wagon of the tail that has been detached
     * or <code>null</code> if it had no wagons attached to its tail.
     */
    public Wagon detachTail() {
        // Detaches the tail if it exists.
        if (hasNextWagon()) {
            // Gets the Wagon behind this one (and every Wagon attached to that one).
            Wagon tailWagon = getNextWagon();

            // Removes the link between this Wagon and it's tail.
            setNextWagon(null);
            tailWagon.detachFront();

            return tailWagon;
        }

        return null; // In the case that the method gets called but there is no Wagon behind this Wagon.
    }

    /**
     * Detaches this wagon from the wagon in front of it.
     * No action if this wagon has no previous wagon attached.
     *
     * @return the former previousWagon that has been detached from,
     * or <code>null</code> if it had no previousWagon.
     */
    public Wagon detachFront() {
        // Detaches the front if it exists.
        if (hasPreviousWagon()) {
            // Gets the Wagon in front of this one (and every Wagon attached to that one)
            Wagon frontWagon = getPreviousWagon();

            // Removes the link between this Wagon and it's front.
            setPreviousWagon(null);
            frontWagon.detachTail();

            return frontWagon;
        }

        return null; // In the case that the method gets called but there is no Wagon in front of this Wagon.
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
        // Check if the wagon already exists in the wagon sequences it needs to be attached to.
        Wagon lastWagon = this;
        while (lastWagon.hasNextWagon()) {
            // Checks if the Wagon is in front.
            if (lastWagon.toString().equals(front.toString()))
                System.err.println("Can't attach wagon to child wagon.");
            // Continues through the sequences to check the next Wagon
            lastWagon = lastWagon.getNextWagon();
        }

        // Detaches any existing connections that will be rearranged.
        detachFront();

        // Attaches this wagon to its new predecessor front.
        front.setNextWagon(this);
        setPreviousWagon(front);
    }

    /**
     * Removes this wagon from the sequence that it is part of,
     * and reconnects its tail to the wagon in front of it, if it exists.
     */
    public void removeFromSequence() {
        Wagon wagonPrevious = getPreviousWagon();
        Wagon wagonNext = getNextWagon();

        // Removes the Wagon from the sequence.
        detachFront();
        detachTail();

        // Attaches the previous and next from the removed object to each other if possible.
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
        Wagon lastBeforeReverse = getLastWagonAttached(); // Will be the first wagon at the end.

        Wagon anchorWagon = this; // Current Wagon during the reverse process.
        Wagon lastWagonPutToFront = null; // Keeps a record of the last wagon we put to the front.

        while (anchorWagon.hasNextWagon()) {
            // Gets the Wagon to move
            Wagon wagonToPutToFront = anchorWagon.getNextWagon();
            // Detaches it from the sequences
            wagonToPutToFront.removeFromSequence();

            // Puts the detaches Wagon in front of the current anchor.
            wagonToPutToFront.attachTail(Objects.requireNonNullElse(lastWagonPutToFront, anchorWagon));

            lastWagonPutToFront = wagonToPutToFront;
        }

        // Attach it back to the starts previous wagon if it exists.
        if (previousWagonOnStart != null && lastWagonPutToFront != null)
            lastWagonPutToFront.reAttachTo(previousWagonOnStart);

        return lastBeforeReverse;
    }

    @Override
    public String toString() {
        return String.format("[Wagon-%d]", getId());
    }

}
