package nl.hva.ict.ads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArcherSorterTest {
    protected Sorter<Archer> sorter = new ArcherSorter();
    protected List<Archer> fewArchers;
    protected List<Archer> manyArchers;
    protected Comparator<Archer> scoringScheme = Archer::compareByHighestTotalScoreWithLeastMissesAndLowestId;

    @BeforeEach
    void setup() {
        ChampionSelector championSelector = new ChampionSelector(1L);
        fewArchers = new ArrayList(championSelector.enrollArchers(23));
        manyArchers = new ArrayList(championSelector.enrollArchers(250));
    }

    @Test
    void selInsSortAndCollectionSortResultInSameOrder() {
        List<Archer> fewSortedArchers = new ArrayList<>(fewArchers);
        List<Archer> manySortedArchers = new ArrayList<>(manyArchers);

        Collections.shuffle(fewSortedArchers);
        sorter.selInsSort(fewSortedArchers, Comparator.comparing(Archer::getId));
        fewArchers.sort(Comparator.comparing(Archer::getId));
        assertEquals(fewArchers, fewSortedArchers);

        sorter.selInsSort(manySortedArchers, Comparator.comparing(Archer::getLastName));
        manyArchers.sort(Comparator.comparing(Archer::getLastName));
        assertEquals(manyArchers.stream().map(Archer::getLastName).collect(Collectors.toList()),
                manySortedArchers.stream().map(Archer::getLastName).collect(Collectors.toList()));

        sorter.selInsSort(fewSortedArchers, scoringScheme);
        fewArchers.sort(scoringScheme);
        assertEquals(fewArchers, fewSortedArchers);

        sorter.selInsSort(manySortedArchers, scoringScheme);
        manyArchers.sort(scoringScheme);
        assertEquals(manyArchers, manySortedArchers);
    }

    @Test
    void quickSortAndCollectionSortResultInSameOrder() {
        List<Archer> fewSortedArchers = new ArrayList<>(fewArchers);
        List<Archer> manySortedArchers = new ArrayList<>(manyArchers);

        Collections.shuffle(fewSortedArchers);
        sorter.quickSort(fewSortedArchers, Comparator.comparing(Archer::getId));
        fewArchers.sort(Comparator.comparing(Archer::getId));
        assertEquals(fewArchers, fewSortedArchers);

        sorter.quickSort(manySortedArchers, Comparator.comparing(Archer::getLastName));
        manyArchers.sort(Comparator.comparing(Archer::getLastName));
        assertEquals(manyArchers.stream().map(Archer::getLastName).collect(Collectors.toList()),
                manySortedArchers.stream().map(Archer::getLastName).collect(Collectors.toList()));

        sorter.quickSort(fewSortedArchers, scoringScheme);
        fewArchers.sort(scoringScheme);
        assertEquals(fewArchers, fewSortedArchers);

        sorter.quickSort(manySortedArchers, scoringScheme);
        manyArchers.sort(scoringScheme);
        assertEquals(manyArchers, manySortedArchers);
    }

    @Test
    void topsHeapSortAndCollectionSortResultInSameOrder() {
        List<Archer> fewSortedArchers = new ArrayList<>(fewArchers);
        List<Archer> manySortedArchers = new ArrayList<>(manyArchers);

        Collections.shuffle(fewSortedArchers);
        sorter.topsHeapSort(5, fewSortedArchers, Comparator.comparing(Archer::getId));
        fewArchers.sort(Comparator.comparing(Archer::getId));
        assertEquals(fewArchers.subList(0,5), fewSortedArchers.subList(0,5));

        sorter.topsHeapSort(1, manySortedArchers, scoringScheme);
        manyArchers.sort(scoringScheme);
        assertEquals(manyArchers.get(0), manySortedArchers.get(0));

        sorter.topsHeapSort(25, manySortedArchers, scoringScheme);
        assertEquals(manyArchers.subList(0,25), manySortedArchers.subList(0,25));
    }

}
