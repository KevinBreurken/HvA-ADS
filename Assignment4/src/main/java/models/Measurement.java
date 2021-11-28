package models;

import java.time.LocalDate;
import java.util.Map;

public class Measurement {
    private final static int FIELD_STN = 0;
    private final static int FIELD_YYMMDDDD = 1;
    private final static int FIELD_FG = 4;
    private final static int FIELD_FXX = 9;
    private final static int FIELD_TG = 11;
    private final static int FIELD_TN = 12;
    private final static int FIELD_TX = 14;
    private final static int FIELD_SQ = 18;
    private final static int FIELD_RH = 22;
    private final static int FIELD_RHX = 23;
    private final static int NUM_FIELDS = 24;

    private final Station station;            // col0, STN
    private final LocalDate date;             // col1, YYMMDDDD
    private double averageWindSpeed;    // col4, FG in m/s  from 0.1 m/s
    private double maxWindGust;         // col9, FXX in m/s  from 0.1 m/s
    private double averageTemperature;  // col11, TG in degC  from 0.1 degC
    private double minTemperature;      // col12, TN in degC  from 0.1 degC
    private double maxTemperature;      // col14, TX in degC  from 0.1 degC
    private double solarHours;          // col18, SQ in hours  from 0.1 h
    private double precipitation;       // col22, RH in mm  from 0.1 mm, -1 = < 0.05
    private double maxHourlyPrecipitation;   // col23, RHX in mm  from 0.1 mm, -1 = < 0.05

    public Measurement(Station station, int dateNumber) {
        this.station = station;
        this.date = LocalDate.of(dateNumber / 10000, (dateNumber / 100) % 100, dateNumber % 100);
    }

    /**
     * converts a text line into a new Measurement instance
     * processes columns # STN, YYYYMMDD, FG, FXX, TG, TN, TX, SQ, RH, RHX as per documentation in the text files
     * converts integer values to doubles as per unit of measure indicators
     * empty or corrupt values are replaced by Double.NaN
     * -1 values that indicate < 0.05 are replaced by 0.0
     * @param textLine
     * @param stations  a map of Stations that can be accessed by station number STN
     * @return          a new Measurement instance that records all data values of above quantities
     *                  null if the station number cannot be resolved,
     *                      or the record is incomplete or cannot be parsed
     */
    public static Measurement fromLine(String textLine, Map<Integer,Station> stations) {
        String[] fields = textLine.split(",");
        if (fields.length < NUM_FIELDS) return null;

        // TODO create a new Measurement instance
        //  further parse and convert and store all relevant quantities



        return null;
    }

    public Station getStation() {
        return station;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAverageWindSpeed() {
        return averageWindSpeed;
    }

    public void setAverageWindSpeed(double averageWindSpeed) {
        this.averageWindSpeed = averageWindSpeed;
    }

    public double getMaxWindGust() {
        return maxWindGust;
    }

    public void setMaxWindGust(double maxWindGust) {
        this.maxWindGust = maxWindGust;
    }

    public double getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(double averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getSolarHours() {
        return solarHours;
    }

    public void setSolarHours(double solarHours) {
        this.solarHours = solarHours;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    public double getMaxHourlyPrecipitation() {
        return maxHourlyPrecipitation;
    }

    public void setMaxHourlyPrecipitation(Double maxHourlyPrecipitation) {
        this.maxHourlyPrecipitation = maxHourlyPrecipitation;
    }
}
