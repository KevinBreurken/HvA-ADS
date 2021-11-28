package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.function.Function;

public class ClimateTracker {
    private final String MEASUREMENTS_FILE_PATTERN = ".*\\.txt";

    private Map<Integer,Station> stations;        // all available weather stations organised by Station Number (STN)

    public Set<Station> getStations() {
        // TODO return all stations in this tracker


        return Set.of();
    }
    public Station findStationById(int stn) {
        // TODO find the station with the given stn


        return null;
    }
    public ClimateTracker() {
        this.stations = new HashMap<>();
    }

    /**
     * calculates for each station how many Measurement instances have been registered
     * @return
     */
    public Map<Station,Integer> numberOfMeasurementsByStation() {
        // TODO build a map resolving for each station its number of registered Measurement instances


        return null;
    }

    /**
     * calculates for each station the date of the first day of its measurements
     * stations without measurements shall be excluded from the result
     * @return
     */
    public Map<Station,LocalDate> firstDayOfMeasurementByStation() {
        // TODO build a map resolving for each station the date of its first day of measurements


        return null;
    }

    /**
     * calculates for each station how many valid values it has available for the measurement quantity
     * that can be accessed by the mapper.
     * invalid values are registered as Double.NaN. They originate from empty or corrupt data in the source file
     * @param mapper    a getter method that accesses the selected quantity from a Measurement instance
     * @return
     */
    public Map<Station,Integer> numberOfValidValuesByStation(Function<Measurement,Double> mapper) {
        // TODO build a map resolving for each station the number of valid values for the specified quantity.


        return null;
    }

    /**
     * Calculates for each calendar year in the dataset the average daily temperatures
     * across all days in the year and all stations in this tracker
     * (invalid values shall be excluded from the averaging)
     * @return      a map(Y,T) that provides for each year Y the average temperature T of that year
     */
    public Map<Integer,Double> annualAverageTemperatureTrend() {
        // TODO build a map collecting for each year the average temperature in that year


        return null;
    }

    /**
     * Calculates for each calendar year in the dataset the maximum of the selected daily quantity
     * across all days in the year and all stations in this tracker
     * (invalid values shall be excluded from the maximum aggregation)
     * (this method can be reused for maximum aggregation of different quantities in the Measurement data
     * @param mapper a getter function on the Measurement class
     *              that selects the appropriate value for the maximum aggregation procedure
     * @return      a map(Y,Q) that provides for each year Y the maximum value Q of the specified quantity
     */
    public Map<Integer,Double> annualMaximumTrend(Function<Measurement,Double> mapper) {
        // TODO build a map collecting for each year the maximum value of the mapped quantity in that year


        return null;
    }

    /**
     * Calculates for each of the 12 calendar months the average daily hours of sunshine
     * across all years and all stations
     * The graph of these numbers can be found in touristic brochures.
     * (invalid values shall be excluded from the averaging)
     * @return      a map(M,SQ) that provides for each month M the average daily sunshine hours SQ across all times
     */
    public Map<Month,Double> allTimeAverageDailySolarByMonth() {
        // TODO build a map collecting for each month the average value of daily sunshine hours


        return null;
    }

    /**
     * calculate the coldest year of all times.
     * The coldest year is defined as the year with the lowest total sum of daily minimum temperatures below zero
     * accumulated across all stations
     * I.e. any daily value of a minimum temperature at a station that is above zero should not be included in its sum for the year
     * The lowest yearsum of negative minimum temperatures indicates the coldest year.
     * @return      the coldest year (a number between 1900 and 2099)
     *              return -1 if no valid minimum temperature measurements are available
     */
    public int coldestYear() {
        // TODO determine the coldest year
        //  hint: first build a helper map that accumulates the yearsums of negative minimum temperatures
        //        then find the coldest year in that helper map



        return -1;
    }

    /**
     * imports all station and measurement information
     * @param folderPath
     */
    public void importClimateDataFromVault(String folderPath) {
        this.stations.clear();

        // load all stations from the text file
        importItemsFromFile(this.stations,
                folderPath + "/stations.txt", null,
                Station::fromLine, Station::getStn);

        // load all measurements from the folder
        importMeasurementsFromVault(folderPath + "/measurements");
    }

    /**
     * traverses the purchases vault recursively and processes every data file that it finds
     * @param filePath
     */
    public void importMeasurementsFromVault(String filePath) {

        File file = new File(filePath);

        if (file.isDirectory()) {
            // the file is a folder (a.k.a. directory)
            //  retrieve a list of all files and sub folders in this directory
            File[] filesInDirectory = Objects.requireNonNullElse(file.listFiles(), new File[0]);

            // merge all purchases of all files and sub folders from the filesInDirectory list, recursively.
            for (File f : filesInDirectory) {
                this.importMeasurementsFromVault(f.getAbsolutePath());
            }
        } else if (file.getName().matches(MEASUREMENTS_FILE_PATTERN)) {
            // the file is a regular file that matches the target pattern for raw purchase files
            // merge the content of this file into this.purchases
            this.importMeasurementsFromFile(file.getAbsolutePath());
        }
    }

    /**
     * imports a collection of items from a text file which provides one line for each item
     * @param items         the list to which imported items shall be added
     * @param filePath      the file path of the source text file
     * @param converter     a function that can convert a text line into a new item instance
     * @param <E>           the (generic) type of each item
     */
    private static <K,E> void importItemsFromFile(Map<K,E> items, String filePath, String headerPrefix,
                                                 Function<String,E> converter, Function<E,K> mapper) {
        int originalNumItems = items.size();

        Scanner scanner = createFileScanner(filePath);

        // skip lines until the header is hit
        while (headerPrefix != null && scanner.hasNext()) {
            // import another line of the header
            String line = scanner.nextLine();

            if (line.startsWith(headerPrefix)) headerPrefix = null;
        }

        //  read all remaining source lines from the scanner,
        //  convert each line to an item of type E and
        //  and add each item to the map
        int newCount = 0;
        while (scanner.hasNext()) {
            // input another line
            String line = scanner.nextLine();

            // convert the line into an instance of E
            E newItem = converter.apply(line);

            //System.out.printf("extracted %s from line: %s\n", newItem, line);

            //  put the item into the map after successful conversion,
            //  using the mapper to determine the key
            //  do not overwrite existing items in the map
            if (newItem != null) {
                items.putIfAbsent(mapper.apply(newItem), newItem);
                newCount++;
            }
        }

        if (items.size() < originalNumItems+newCount) {
            throw new InputMismatchException(String.format("Duplicate items found in file %s", filePath));
        }

        //System.out.printf("Imported %d items from %s.\n", items.size() - originalNumItems, filePath);
    }

    /**
     * imports another batch of raw purchase data from the filePath text file
     * and merges the purchase amounts with the earlier imported and accumulated collection in this.purchases
     * @param filePath
     */
    private void importMeasurementsFromFile(String filePath) {

        // create a temporary map to import the measurements, organised by date
        Map<LocalDate, Measurement> newMeasurementsByDate = new HashMap<>();

        // import all measurements from the specified file into the newMeasurementsByDate map
        importItemsFromFile(newMeasurementsByDate, filePath, "# STN",
                s -> Measurement.fromLine(s, this.stations), Measurement::getDate);

        //  add the measurements to their station
        //  (all measurements from the same file should belong to the same station)
        if (newMeasurementsByDate.size() > 0) {
            Station station = newMeasurementsByDate.values().stream().findAny().get().getStation();
            // add the newMeasurements to the map in the station
            int numAdded = station.addMeasurements(newMeasurementsByDate.values());
            if (numAdded != newMeasurementsByDate.size()) {
                throw new InputMismatchException(String.format("Some items in file %s could not be added", filePath));
            }
        }
    }

    /**
     * helper method to create a scanner on a file an handle the exception
     * @param filePath
     * @return
     */
    private static Scanner createFileScanner(String filePath) {
        try {
            return new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFound exception on path: " + filePath);
        }
    }
}
