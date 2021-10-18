package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;

public class OrderedArrayList<E>
        extends ArrayList<E>
        implements OrderedList<E> {

    protected Comparator<? super E> ordening;   // the comparator that has been used with the latest sort
    protected int nSorted;                      // the number of items that have been ordered by barcode in the list
    // representation-invariant
    //      all items at index positions 0 <= index < nSorted have been ordered by the given ordening comparator
    //      other items at index position nSorted <= index < size() can be in any order amongst themselves
    //              and also relative to the sorted section
    int low, high; //Variables needed for the recursive binary search

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

    private void setBinarySearchVariablesToDefault() {
        low = 0;
        high = nSorted-1;
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
        if(index <= this.nSorted)
            this.nSorted = index;
    }

    @Override
    public E remove(int index) {
        E element = get(index);
        this.remove(element);

        return element;
    }

    @Override
    public boolean remove(Object o) {
        if(indexOf(o) <= this.nSorted)
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
            // some arbitrary choice to use the iterative or the recursive version
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

        //Stop if we don't have any elements
        if(high < 1)
            return -1;

        int low = 0, high = nSorted-1, mid, compareValue;
//        int mid, compareValue;

        while (low <= high && high < nSorted) {
            //Calculates the index number that is in the middle of the range.
            mid = low + (high - low) / 2;

            //TODO remove test code
//            System.out.printf("L[%s] M[%s] H[%s]%n",low,mid,high);
//            System.out.println(this.ordening.compare(searchItem, get(high)));

            //Compares the item in the middle to the given item.
            compareValue = this.ordening.compare(searchItem, get(mid));

            //Sets the lowest and highest values of the range to check next.
            if (compareValue == 0) {
                return mid;
            } else if (compareValue > 0) {
                low = mid+1;
            } else {
                high = mid-1;
            }
        }
        System.out.println("CALLING LINEAR SEARCH");
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
     * @param searchItem the item to be searched on the basis of comparison by this.ordening
     * @return the position index of the found item in the arrayList, or -1 if no item matches the search item.
     */
    public int indexOfByRecursiveBinarySearch(E searchItem) {
        // Search on the sorted section of the arrayList, 0 <= index < nSorted
        // and find the position of an item that matches searchItem (this.ordening comparator yields a 0 result)

        //Stop if we don't have any elements
        if(high < 1)
            return -1;

        int mid = low + (high - low) / 2;

        int compareValue = this.ordening.compare(searchItem, get(mid));

        System.out.printf("L[%s] M[%s] H[%s]%n",low,mid,high);

        if (low <= high && high < nSorted) {
            if (compareValue == 0) {
                return mid;
            } else if (compareValue > 0) {
                low = mid+1;
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
     * @param searchItem
     * @return
     */
    private int linearSearch(E searchItem, List<E> sublist) {
        for (int i = 0; i < sublist.size(); i++) {
            if (this.ordening.compare(searchItem,sublist.get(i)) == 0) return i + nSorted;
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

        setBinarySearchVariablesToDefault();
        int matchedItemIndex = this.indexOfByRecursiveBinarySearch(newItem);
        System.out.println("Matched Index: " + matchedItemIndex);
        if (matchedItemIndex < 0)
            this.add(newItem);
        else
            this.set(matchedItemIndex,merger.apply(get(matchedItemIndex),newItem));

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ordered ArrayList:\n");
        for (E e : this) {
            sb.append(e + "\n");
        }
        sb.append("\n");

        return sb.toString();
    }
}
