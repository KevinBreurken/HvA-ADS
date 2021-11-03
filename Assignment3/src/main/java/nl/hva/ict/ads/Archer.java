package nl.hva.ict.ads;

public class Archer {
    public static int MAX_ARROWS = 3;
    public static int MAX_ROUNDS = 10;


    private static int LAST_ID = 135788;
    private final int id;
    private final String firstName;
    private final String lastName;

    // TODO add instance variable(s) to track the scores per round per arrow
    int[][] scores;

    /**
     * Constructs a new instance of Archer and assigns a unique id to the instance.
     * Each new instance should be assigned a number that is 1 higher than the last one assigned.
     * The first instance created should have ID 135788;
     *
     * @param firstName the archers first name.
     * @param lastName  the archers surname.
     */
    public Archer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = ++Archer.LAST_ID;

        scores = new int[MAX_ROUNDS][MAX_ARROWS];
    }

    /**
     * Registers the points for each of the three arrows that have been shot during a round.
     *
     * @param round  the round for which to register the points. First round has number 1.
     * @param points the points shot during the round, one for each arrow.
     */
    public void registerScoreForRound(int round, int[] points) {
        if(points.length != MAX_ARROWS) {
            System.err.printf("Points array are of incorrect size [%d]",points.length);
            return;
        }

        if(round < 1 || round > MAX_ROUNDS){
            System.err.printf("Incorrect round given for registering scores. [%d]",round);
            return;
        }

        scores[round-1] = points.clone();
    }


    /**
     * Calculates/retrieves the total score of all arrows across all rounds
     *
     * @return
     */
    public int getTotalScore() {
        // TODO reduce cyclical complexity.
        int val = 0;

        for (int[] score : scores) {
            for (int i : score) {
                val += i;
            }
        }

        return val;
    }

    /**
     * compares the scores/id of this archer with the scores/id of the other archer according to
     * the scoring scheme: highest total points -> least misses -> earliest registration
     * The archer with the lowest id has registered first
     *
     * @param other the other archer to compare against
     * @return negative number, zero or positive number according to Comparator convention
     */
    public int compareByHighestTotalScoreWithLeastMissesAndLowestId(Archer other) {

        int compareValue = other.getTotalScore() - getTotalScore();

        if(compareValue != 0)
            return compareValue;

        compareValue = getAmountOfTotalMisses() - other.getAmountOfTotalMisses();

        if(compareValue != 0)
            return compareValue;

        return id - other.getId();
    }

    public int getAmountOfTotalMisses(){
        int total = 0;

        for (int[] score : scores) {
            for (int i : score) {
               if(i == 0)
                   total++;
            }
        }

        return total;
    }


    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // TODO provide a toSting implementation to format archers nicely

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s (%d) %s %s", getId(), getTotalScore(), getFirstName(), getLastName()));

//        for (int i = 0; i < scores.length; i++) {
//            sb.append(String.format("\t(%d) (%d) (%d)\n", scores[i][0],scores[i][1],scores[i][2]));
//        }

        return sb.toString();
    }
}
