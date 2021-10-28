package nl.hva.ict.ads;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArcherTest {

    private static Comparator<Archer> scoringScheme;
    Archer archer1, archer2, archer3, archer4;
    int[] scores1, scores2, scores3, scores4;

    @BeforeAll
    static void setupClass() {
        scoringScheme = Archer::compareByHighestTotalScoreWithLeastMissesAndLowestId;
    }

    @BeforeEach
    void setup() {
        archer1 = new Archer("Sjef van den", "Berg");
        scores1 = new int[]{10,8,0};
        archer2 = new Archer("Nico", "Tromp");
        scores2 = new int[]{9,9,0};
        archer3 = new Archer("Gabriëla", "Schloesser");
        scores3 = new int[]{9,6,3};
        archer4 = new Archer("Steve", "Wijler");
        scores4 = new int[]{5,5,5};
    }

    @Test
    void archerIdsIncreaseCorrectly() {
        assertEquals(archer2.getId(), archer1.getId()+ 1);
        assertEquals(archer3.getId(), archer2.getId()+ 1);
    }

    @Test
    void idFieldShouldBeUnchangeable() throws NoSuchFieldException {
        assertTrue((Archer.class.getDeclaredField("id").getModifiers() & 0x00000010) != 0);
    }

    @Test
    void checkToString() {
        //System.out.println(archer1);
        String expectedInfo = archer1.getId()
                + " (" + archer1.getTotalScore() + ") "
                + archer1.getFirstName() + " " + archer1.getLastName();

        assertEquals(expectedInfo, archer1.toString());
    }

    @Test
    public void checkTotalScore() {
        for (int round = 1; round <= Archer.MAX_ROUNDS; round++) {
            archer1.registerScoreForRound(round, scores1);
            archer4.registerScoreForRound(round, scores4);
        }

        assertEquals(180, archer1.getTotalScore());
        assertEquals(150, archer4.getTotalScore());
    }

    @Test
    void checkTotalScoreUpdates() {
        for (int round = 1; round <= Archer.MAX_ROUNDS; round++) {
            archer1.registerScoreForRound(round, scores1);

        }
        archer1.registerScoreForRound(1,scores2);
        assertEquals(180, archer1.getTotalScore());
        archer1.registerScoreForRound(2,scores4);
        assertEquals(177, archer1.getTotalScore());
        archer1.registerScoreForRound(10,scores4);
        assertEquals(174, archer1.getTotalScore());
        archer1.registerScoreForRound(2,scores2);
        assertEquals(177, archer1.getTotalScore());
    }

    @Test
    void archersShouldKeepTheirOwnPoints() {
        int[] points = {10, 10, 10};
        archer1.registerScoreForRound(1, points);
        points[0] = 8;
        points[1] = 8;
        points[2] = 6;
        archer2.registerScoreForRound(1, points);

        assertEquals(30, archer1.getTotalScore());
        assertEquals(22, archer2.getTotalScore());
    }

    @Test
    void comparisonDifferentTotalScore() {
        for (int round = 1; round <= Archer.MAX_ROUNDS; round++) {
            archer1.registerScoreForRound(round, scores1);
            archer4.registerScoreForRound(round, scores4);
        }

        assertTrue(scoringScheme.compare(archer1, archer4) < 0);
    }

    @Test
    void comparisonLessMissesIsWinner() {
        for (int round = 1; round <= Archer.MAX_ROUNDS; round++) {
            archer2.registerScoreForRound(round, scores2);
            archer3.registerScoreForRound(round, scores3);
        }
        assertTrue(scoringScheme.compare(archer2, archer3) > 0);
    }

    @Test
    void comparisonFirstRegisteredIsWinner() {
        for (int round = 1; round <= Archer.MAX_ROUNDS; round++) {
            archer1.registerScoreForRound(round, scores1);
            archer2.registerScoreForRound(round, scores2);
        }
        assertTrue(scoringScheme.compare(archer1, archer2) < 0);
    }
}
