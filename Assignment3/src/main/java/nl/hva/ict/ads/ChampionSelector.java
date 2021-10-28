package nl.hva.ict.ads;

import java.util.*;

public class ChampionSelector {
    private Random randomizer;

    private List<Archer> archers = new ArrayList<>();
    public List<Archer> getArchers() { return archers; }

    public ChampionSelector(long seed) {
        randomizer = new Random(seed);
        Names.reSeed(randomizer.nextLong());
    }


    /**
     * Enrolls all nrOfArchers into the champion selector of the competition
     * Every archer is registered by id, first name and last name.
     * Every archer shoots all of its rounds and registers its scores
     * @param nrOfArchers the number of archers in the list.
     * @return      the complete list of all archers that have been registered in this competition
     */
    public List<Archer> enrollArchers(int nrOfArchers) {
        for (int i = 0; i < nrOfArchers; i++) {
            Archer archer = new Archer(Names.nextFirstName(), Names.nextSurname());
            archers.add(archer);
            letArcherShootAllRounds(archer);
        }
        return archers;
    }

    /**
     * Calculates and shows key results of the competition
     */
    public void showResults() {
        Sorter<Archer> sorter = new ArcherSorter();
        System.out.printf("%d archers have participated in this competition\n", archers.size());

        Collections.shuffle(archers);
        sorter.selInsSort(archers, Comparator.comparing(Archer::getId));
        System.out.printf("The first three archers to enroll were: %s\n", archers.subList(0,3));

        sorter.selInsSort(archers, Comparator.comparing(Archer::getLastName).thenComparing(Archer::getFirstName));
        System.out.printf("The first three archers by alphabet are: %s\n", archers.subList(0,3));

        sorter.quickSort(archers, Archer::compareByHighestTotalScoreWithLeastMissesAndLowestId);
        System.out.printf("At 4th thru 10th place of the rankings we find: %s\n", archers.subList(3,10));

        Collections.shuffle(archers);
        sorter.topsHeapSort(3, archers, Archer::compareByHighestTotalScoreWithLeastMissesAndLowestId);
        System.out.printf("The top-3 price winners of the competition are: %s\n", archers.subList(0,3));
    }

    /**
     * Simulates the archer shooting all rounds and register the scores confirmed by the judges
     * @param archer
     */
    public void letArcherShootAllRounds(Archer archer) {
        for (int round = 1; round <= Archer.MAX_ROUNDS; round++) {
            archer.registerScoreForRound(round, shootOneRound());
        }
    }

    private int[] shootOneRound() {
        int[] points = new int[Archer.MAX_ARROWS];
        for (int arrow = 0; arrow < Archer.MAX_ARROWS; arrow++) {
            points[arrow] = shootOneArrow();
        }
        return points;
    }

    private int shootOneArrow() {
        return randomizer.nextInt(11);
    }
}
