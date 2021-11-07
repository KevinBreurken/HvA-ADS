package nl.hva.ict.ads;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SorterImpl<E> implements Sorter<E> {

    /**
     * Sorts all items by selection or insertion sort using the provided comparator
     * for deciding relative ordening of two items
     * Items are sorted 'in place' without use of an auxiliary list or array
     *
     * @param items
     * @param comparator
     * @return the items sorted in place
     */
    public List<E> selInsSort(List<E> items, Comparator<E> comparator) {
        int n = items.size();
        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0 && less(comparator, items.get(j), items.get(j - 1)); j--) {
                exchange(items, j, j - 1);
            }
        }

        return items;
    }

    private boolean less(Comparator<E> comparator, E itemA, E itemB) {
        return comparator.compare(itemA, itemB) < 0;
    }

    private void exchange(List<E> items, int i, int j) {
        E item = items.get(i);
        items.set(i, items.get(j));
        items.set(j, item);
    }

    /**
     * Sorts all items by quick sort using the provided comparator
     * for deciding relative ordening of two items
     * Items are sorted 'in place' without use of an auxiliary list or array
     *
     * @param items given list of items
     * @param comparator handles the comparing of the given items
     * @return the items sorted in place
     */
    public List<E> quickSort(List<E> items, Comparator<E> comparator) {
        Collections.shuffle(items);
        // sort the complete list of items from position 0 till size-1, including position size
        this.quickSortPart(items, 0, items.size() - 1, comparator);
        return items;
    }

    /**
     * Sorts all items between index positions 'from' and 'to' inclusive by quick sort using the provided comparator
     * for deciding relative ordening of two items
     * Items are sorted 'in place' without use of an auxiliary list or array or other positions in items
     *
     * @param items given list of items
     * @param comparator handles the comparing of the given items
     * @return the items sorted in place
     */
    private void quickSortPart(List<E> items, int from, int to, Comparator<E> comparator) {
        if (to <= from) return;

        // sets the pivot; the item that is already in place.
        int pivot = partition(items, from, to, comparator);

        // sorts both sides of the pivot.
        quickSortPart(items, from, pivot - 1, comparator);
        quickSortPart(items, pivot + 1, to, comparator);
    }

    /**
     * Helper-function for the quick-sort.
     * Returns a range of items where nothing to the left of the returned item is higher than it
     * and nothing to the right is lower than the item.
     * This item is the pivot, as this item is in the right spot and other items still need to be rearranged.
     * @param items list of items
     * @param from first item in the list to check from
     * @param to last item in the list to check from
     * @param comparator handles the comparing of the given items
     * @return the pivot
     */
    private int partition(List<E> items, int from, int to, Comparator<E> comparator) {
        int i = from, j = to + 1;

        while (true) {
            // increases i by 1 while the item at the index is lower than the first item in the range.
            while (less(comparator, items.get(++i), items.get(from)))
                if (i == to) break;

            // opposite of previous statement.
            while (less(comparator, items.get(from), items.get(--j)))
                if (j == from) break;

            if (i >= j) break; 
            exchange(items, i, j);
        }

        // swap j with the partitioning item
        exchange(items, from, j);
        // returns the index of the item that is the pivot
        return j;
    }

    /**
     * Identifies the lead collection of numTops items according to the ordening criteria of comparator
     * and organizes and sorts this lead collection into the first numTops positions of the list
     * with use of (zero-based) heapSwim and heapSink operations.
     * The remaining items are kept in the tail of the list, in arbitrary order.
     * Items are sorted 'in place' without use of an auxiliary list or array or other positions in items
     *
     * @param numTops    the size of the lead collection of items to be found and sorted
     * @param items
     * @param comparator
     * @return the items list with its first numTops items sorted according to comparator
     * all other items >= any item in the lead collection
     */
    public List<E> topsHeapSort(int numTops, List<E> items, Comparator<E> comparator) {
        // check 0 < numTops <= items.size()
        if (numTops <= 0) return items;
        else if (numTops > items.size()) return quickSort(items, comparator);

        // the lead collection of numTops items will be organised into a (zero-based) heap structure
        // in the first numTops list positions using the reverseComparator for the heap condition.
        // that way the root of the heap will contain the worst item of the lead collection
        // which can be compared easily against other candidates from the remainder of the list
        Comparator<E> reverseComparator = comparator.reversed();

        // initialise the lead collection with the first numTops items in the list
        for (int heapSize = 2; heapSize <= numTops; heapSize++) {
            // repair the heap condition of items[0..heapSize-2] to include new item items[heapSize-1]
            heapSwim(items, heapSize, reverseComparator);
        }

        // insert remaining items into the lead collection as appropriate
        for (int i = numTops; i < items.size(); i++) {
            // loop-invariant: items[0..numTops-1] represents the current lead collection in a heap data structure
            //  the root of the heap is the currently trailing item in the lead collection,
            //  which will lose its membership if a better item is found from position i onwards
            E item = items.get(i);
            E worstLeadItem = items.get(0);
            if (comparator.compare(item, worstLeadItem) < 0) {
                // item < worstLeadItem, so shall be included in the lead collection
                items.set(0, item);
                // demote worstLeadItem back to the tail collection, at the orginal position of item
                items.set(i, worstLeadItem);
                // repair the heap condition of the lead collection
                heapSink(items, numTops, reverseComparator);
            }
        }

        // the first numTops positions of the list now contain the lead collection
        // the reverseComparator heap condition applies to this lead collection
        // now use heapSort to realise full ordening of this collection
        for (int i = numTops - 1; i > 0; i--) {
            // loop-invariant: items[i+1..numTops-1] contains the tail part of the sorted lead collection
            // position 0 holds the root item of a heap of size i+1 organised by reverseComparator
            // this root item is the worst item of the remaining front part of the lead collection

            // TODO swap item[0] and item[i];
            //  this moves item[0] to its designated position


            // TODO the new root may have violated the heap condition
            //  repair the heap condition on the remaining heap of size i

        }
        // alternatively we can realise full ordening with a partial quicksort:
        // quickSortPart(items, 0, numTops-1, comparator);

        return items;
    }

    /**
     * Repairs the zero-based heap condition for items[heapSize-1] on the basis of the comparator
     * all items[0..heapSize-2] are assumed to satisfy the heap condition
     * The zero-bases heap condition says:
     * all items[i] <= items[2*i+1] and items[i] <= items[2*i+2], if any
     * or equivalently:     all items[i] >= items[(i-1)/2]
     *
     * @param items
     * @param heapSize
     * @param comparator
     */
    private void heapSwim(List<E> items, int heapSize, Comparator<E> comparator) {
        // TODO swim items[heapSize-1] up the heap until
        //      i==0 || items[(i-1]/2] <= items[i]

    }

    /**
     * Repairs the zero-based heap condition for its root items[0] on the basis of the comparator
     * all items[1..heapSize-1] are assumed to satisfy the heap condition
     * The zero-bases heap condition says:
     * all items[i] <= items[2*i+1] and items[i] <= items[2*i+2], if any
     * or equivalently:     all items[i] >= items[(i-1)/2]
     *
     * @param items
     * @param heapSize
     * @param comparator
     */
    private void heapSink(List<E> items, int heapSize, Comparator<E> comparator) {
        // TODO sink items[0] down the heap until
        //      2*i+1>=heapSize || (items[i] <= items[2*i+1] && items[i] <= items[2*i+2])

    }
}
