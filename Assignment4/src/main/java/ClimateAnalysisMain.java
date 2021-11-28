import models.ClimateTracker;
import models.Measurement;

import java.time.LocalDate;
import java.util.Map;

public class ClimateAnalysisMain {

    public static void main(String[] args) {
        System.out.println("Welcome to the HvA Climate Analyser");

        ClimateTracker climateTracker = new ClimateTracker();

        climateTracker.importClimateDataFromVault(ClimateAnalysisMain.class.getResource("/knmi").getPath());

        System.out.println("\n1. Total Number of measurements by station:\n" +
                climateTracker.numberOfMeasurementsByStation());
        System.out.println("\n2. First day of measurement by station:\n" +
                climateTracker.firstDayOfMeasurementByStation());

        System.out.printf("\n3. All-time maximum temperature in de Bilt = %.1f degC\n",
                climateTracker.findStationById(260).allTimeMaxTemperature());

        System.out.println("\n4. Number of valid daily precipitation measurements by station:\n" +
                climateTracker.numberOfValidValuesByStation(Measurement::getPrecipitation));
        System.out.printf("\n5. Total precipication in de Bilt in 1963 = %.0f mm\n",
                climateTracker.findStationById(260).totalPrecipitationBetween(LocalDate.of(1963,1,1), LocalDate.of(1963,12,31)));

        System.out.printf("\n6. Annual trend of average temperatures (in degC):\n%s\n",
                climateTracker.annualAverageTemperatureTrend());
        System.out.printf("\n7. Annual trend of maximum hourly precipitation (in mm):\n%s\n",
                climateTracker.annualMaximumTrend(Measurement::getMaxHourlyPrecipitation));
        System.out.printf("\n8. Annual trend of maximum wind gust (in m/s):\n%s\n",
                climateTracker.annualMaximumTrend(Measurement::getMaxWindGust));

        System.out.printf("\n9. All-time monthly profile of daily solar hours:\n%s\n",
                climateTracker.allTimeAverageDailySolarByMonth());

        System.out.printf("\n10. Coldest year = %d\n", climateTracker.coldestYear());

        //mapToCSV("Annual average temperature trend", climateTracker.annualAverageTemperatureTrend());
        //mapToCSV("Annual max hourly precipitation trend", climateTracker.annualMaximumTrend(Measurement::getMaxHourlyPrecipitation));
        //mapToCSV("Annual max wind gust trend", climateTracker.annualMaximumTrend(Measurement::getMaxWindGust));
    }

    private static <K> void mapToCSV(String title, Map<K,Double> data) {
        System.out.printf("\n%s data in csv format:\n", title);
        for (Map.Entry<K,Double> e : data.entrySet()) {
            System.out.printf("%s;%.1f\n", e.getKey(), e.getValue());
        }
    }
}
