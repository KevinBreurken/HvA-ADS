package nl.hva.ict.ads;

public class ArcheryMain {
    public static void main(String[] args) {
        System.out.println("Welcome to the HvA Archery Champion Selector\n");

        Archer temp = new Archer("Nico","Tromp");
        temp.registerScoreForRound(1,new int[]{4,10,90});
        Archer temp2 = new Archer("Nica","Trimp");
        temp2.registerScoreForRound(1,new int[]{4,10,90});

        System.out.println("T1:" + temp.getTotalScore() + " : " + temp.getAmountOfTotalMisses());

        System.out.println("T2:" + temp2.getTotalScore() + " : " + temp2.getAmountOfTotalMisses());

        System.out.println(temp.compareByHighestTotalScoreWithLeastMissesAndLowestId(temp2));
//        ChampionSelector championSelector = new ChampionSelector(19670427L);
//        championSelector.enrollArchers(1001);
//        championSelector.showResults();
    }
}
