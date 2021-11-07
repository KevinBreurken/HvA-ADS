package nl.hva.ict.ads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class ArcherPerformanceTest {
    protected Sorter<Archer> sorter = new ArcherSorter();
    protected List<Archer> archers;

    @BeforeEach
    void setup() {
        System.gc();
        ChampionSelector championSelector = new ChampionSelector(1L);
        archers = new ArrayList(championSelector.enrollArchers(819200));
    }

    @Test
    void PERFORMANCE_measureQuickSortPerformance() {
        sorter.quickSort(archers, Comparator.comparing(Archer::getId));
    }

    @Test
    void PERFORMANCE_measureSelectionSortPerformance() {
        sorter.selectionSort(archers, Comparator.comparing(Archer::getId));
    }

    @Test
    void PERFORMANCE_measureInsertionSortPerformance() {
        sorter.insertionSort(archers, Comparator.comparing(Archer::getId));
    }

}
