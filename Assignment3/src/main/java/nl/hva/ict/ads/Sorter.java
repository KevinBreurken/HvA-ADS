package nl.hva.ict.ads;

import java.util.Comparator;
import java.util.List;

public interface Sorter<E> {
    List<E> selInsSort(List<E> items, Comparator<E> comparator);
    List<E> quickSort(List<E> items, Comparator<E> comparator);
    default List<E> topsHeapSort(int numTops, List<E> items, Comparator<E> comparator) {
        return quickSort(items, comparator);
    }
}
