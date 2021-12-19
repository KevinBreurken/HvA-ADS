import models.Measurement;
import models.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class MeasurementTest {

    Station deBilt, maastricht;
    Map<Integer,Station> stations;

    @BeforeEach
    private void setup() {
        deBilt = new Station(260, "De Bilt");
        maastricht = new Station(380, "Maastricht");
        stations = Map.of(260, deBilt, 380, maastricht);
    }

    @Test
    public void canConvertATextLineToAMeasurement() {
        Measurement measurement1 = Measurement.fromLine("  380,19060204,   95,   36,   36,   77,   12,    5,   24,     ,     ,  -23,  -59,     ,   26,     ,     ,     ,   40,   50,     ,     ,     ,     ,     ,10218,10251,     ,10196,     ,     ,     ,     ,     ,     ,   47,     ,     ,     ,     ,     ", stations);
        checkMeasurement(measurement1, maastricht, 1906, 2, 4,
                                        3.6, Double.NaN,
                                        -2.3, -5.9, 2.6,
                                        4.0,
                                        Double.NaN, Double.NaN);
        Measurement measurement2 = Measurement.fromLine("  380,19750914,  204,   87,   87,  123,   14,   51,   20,  185,   14,  141,  113,   24,  176,   14,  100,     ,   14,   11,  944,    5,    6,    6,     , 9991,10046,     , 9959,     ,   70,    1,   82,    2,    7,   75,   94,    1,   56,   14,   15", stations);
        checkMeasurement(measurement2, maastricht, 1975, 9, 14,
                8.7, 18.5,
                14.1, 11.3, 17.6,
                1.4,
                0.6, 0.6);
    }

    private void checkMeasurement(Measurement measurement,
                                  Station station, int year, int month, int day,
                                  double fg, double fxx,
                                  double tg, double tn, double tx,
                                  double sq,
                                  double rh, double rhx) {
        assertSame(station, measurement.getStation(), "Station number not properly resolved");
        assertEquals(year, measurement.getDate().getYear(), "Wrong year in measurement date");
        assertEquals(month, measurement.getDate().getMonthValue(), "Wrong month in measurement date");
        assertEquals(day, measurement.getDate().getDayOfMonth(),  "Wrong day number in measurement date");
        assertEquals(fg, measurement.getAverageWindSpeed(), 1E-6, "FG Average wind speed not properly imported");
        assertEquals(fxx, measurement.getMaxWindGust(), 1E-6, "FXX max wind gust not properly imported");
        assertEquals(tg, measurement.getAverageTemperature(), 1E-6, "TG Average temperature not properly imported");
        assertEquals(tn, measurement.getMinTemperature(), 1E-6, "TN minimum temperature not properly imported");
        assertEquals(tx, measurement.getMaxTemperature(), 1E-6, "TX max temperature not properly imported");
        assertEquals(sq, measurement.getSolarHours(), 1E-6, "SQ daily solar hours not properly imported");
        assertEquals(rh, measurement.getPrecipitation(), 1E-6, "RH daily precipitation not properly imported");
        assertEquals(rhx, measurement.getMaxHourlyPrecipitation(), 1E-6, "RHX max hourly precipitation not properly imported");
    }
}