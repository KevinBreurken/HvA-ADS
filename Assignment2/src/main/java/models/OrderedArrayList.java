package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;

public class OrderedArrayList<E> extends ArrayList<E> implements OrderedList<E> {

    protected Comparator<? super E> ordening;   // the comparator that has been used with the latest sort
    protected int nSorted;                      // the number of items that have been ordered by barcode in the list
    private int low, high;                    // Variables needed for the recursive binary search

    public OrderedArrayList() {
        this(null);
    }

    public OrderedArrayList(Comparator<? super E> ordening) {
        super();
        this.ordening = ordening;
        this.nSorted = 0;
    }

    public Comparator<? super E> getOrdening() {
        return this.ordening;
    }

    /**
     * restores the low and high variables for binary searches.
     */
    private void setBinarySearchVariablesToDefault() {
        low = 0;
        high = nSorted - 1;
    }

    @Override
    public void clear() {
        super.clear();
        this.nSorted = 0;
    }

    @Override
    public void sort(Comparator<? super E> c) {
        super.sort(c);
        this.ordening = c;
        this.nSorted = this.size();
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);

        //Sets nSorted if the elements gets places in the sorted section.
        if (index <= this.nSorted) this.nSorted = index;
    }

    @Override
    public E remove(int index) {
        E element = get(index);
        this.remove(element);

        // Lower nSorted if the index was part of the sorted section.
        if (index <= this.nSorted)
            this.nSorted--;

        return element;
    }

    @Override
    public boolean remove(Object o) {
        // Lower nSorted if the index was part of the sorted section.
        if (indexOf(o) <= this.nSorted)
            this.nSorted--;

        return super.remove(o);
    }

    @Override
    public void sort() {
        if (this.nSorted < this.size()) {
            this.sort(this.ordening);
        }
    }

    @Override
    public int indexOf(Object item) {
        if (item != null) {
            return indexOfByIterativeBinarySearch((E) item);
        } else {
            return -1;
        }
    }

    @Override
    public int indexOfByBinarySearch(E searchItem) {
        if (searchItem != null) {
            setBinarySearchVariablesToDefault();
            return indexOfByRecursiveBinarySearch(searchItem);
        } else {
            return -1;
        }
    }

    /**
     * finds the position of the searchItem by an iterative binary search algorithm in the
     * sorted section of the arrayList, using the this.ordening comparator for comparison and equality test.
     * If the item is not found in the sorted section, the unsorted section of the arrayList shall be searched by linear search.
     * The found item shall yield a 0 result from the this.ordening comparator, and that need not to be in agreement with the .equals test.
     * Here we follow the comparator for ordening items and for deciding on equality.
     *
     * @param searchItem the item to be searched on the basis of comparison by this.ordening
     * @return the position index of the found item in the arrayList, or -1 if no item matches the search item.
     */
    public int indexOfByIterativeBinarySearch(E searchItem) {
        setBinarySearchVariablesToDefault();
        int mid, compareValue;

        while (low <= high && high < nSorted) {
            //Calculates the index number that is in the middle of the range that needs to be checked.
            mid = low + (high - low) / 2;

            //Compares the given item to the item in the middle of the range (between low and high).
            compareValue = this.ordening.compare(searchItem, get(mid));

            //Returns if the item has been found.
            if (compareValue == 0) return mid;
            //Sets the lowest and highest values of the range to check next.
            else if (compareValue > 0) low = mid + 1;
            else high = mid - 1;

        }
        //If no match has been found, a linear search will be done on the unsorted section.
        //-1 gets returned if no match has been found here either.
        return linearSearch(searchItem, this.subList(nSorted, this.size()));
    }

    /**
     * finds the position of the searchItem by a recursive binary search algorithm in the
     * sorted section of the arrayList, using the this.ordening comparator for comparison and equality test.
     * If the item is not found in the sorted section, the unsorted section of the arrayList shall be searched by linear search.
     * The found item shall yield a 0 result from the this.ordening comparator, and that need not to be in agreement with the .equals test.
     * Here we follow the comparator for ordening items and for deciding on equality.
     *
     * The helper method setBinarySearchVariablesToDefault() needs to be called beforehand.
     *
     * @param searchItem the item to be searched on the basis of comparison by this.ordening
     * @return the position index of the found item in the arrayList, or -1 if no item matches the search item.
     */
    public int indexOfByRecursiveBinarySearch(E searchItem) {
        if (size() < 1) return -1;

        //Calculates the index number that is in the middle of the range that needs to be checked.
        int mid = low + (high - low) / 2;

        //Compares the given item to the item in the middle of the range (between low and high).
        int compareValue = this.ordening.compare(searchItem, get(mid));

        if (low <= high && high < nSorted) {
            //Returns if the item has been found.
            if (compareValue == 0) return mid;
            //Sets the lowest and highest values of the range to check next and calls itself.
            else if (compareValue > 0) {
                low = mid + 1;
                return indexOfByRecursiveBinarySearch(searchItem);
            } else {
                high = mid - 1;
                return indexOfByRecursiveBinarySearch(searchItem);
            }
        }

        //If no match has been found, a linear search will be done on the unsorted section.
        //-1 gets returned if no match has been found here either.
        return linearSearch(searchItem, this.subList(nSorted, this.size()));
    }

    /**
     * Searches for the item in the given sublist of this instance of this Class and returns it
     * or returns -1 if the item isn't in this part of this list.
     *
     * @param searchItem
     * @return
     */
    private int linearSearch(E searchItem, List<E> sublist) {
        for (int i = 0; i < sublist.size(); i++) {
            if (this.ordening.compare(searchItem, sublist.get(i)) == 0) return i + nSorted;
        }
        return -1;
    }

    /**
     * finds a match of newItem in the list and applies the merger operator with the newItem to that match
     * i.e. the found match is replaced by the outcome of the merge between the match and the newItem
     * If no match is found in the list, the newItem is added to the list.
     *
     * @param newItem
     * @param merger  a function that takes two items and returns an item that contains the merged content of
     *                the two items according to some merging rule.
     *                e.g. a merger could add the value of attribute X of the second item
     *                to attribute X of the first item and then return the first item
     * @return whether a new item was added to the list or not
     */
    @Override
    public boolean merge(E newItem, BinaryOperator<E> merger) {
        if (newItem == null) return false;

        setBinarySearchVariablesToDefault(); //Helper-function for the binary search methods.
        //Searches if the new item exists in this List.
        int matchedItemIndex = this.indexOfByRecursiveBinarySearch(newItem);

        //Merging of the item:
        if (matchedItemIndex < 0) this.add(newItem); //If the item isn't already in the list
        else this.set(matchedItemIndex, merger.apply(newItem, get(matchedItemIndex)));

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ordered ArrayList:\n");
        for (int i = 0; i < size(); i++) {
            sb.append(this.get(i) + "\n");
            if(i == nSorted - 1)
                sb.append("End of sorted segment\n");
        }
        sb.append("\n");

        return sb.toString();
    }
}
