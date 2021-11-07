package nl.hva.ict.ads;

public class ArcheryMain {
    public static void main(String[] args) {
        System.out.println("Welcome to the HvA Archery Champion Selector\n");

        ChampionSelector championSelector = new ChampionSelector(19670427L);
        championSelector.enrollArchers(1001);
        championSelector.showResults();
    }
}
