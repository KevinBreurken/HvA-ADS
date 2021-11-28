import models.Station;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StationTest {

    Station deBilt, vlissingen;

    @BeforeEach
    private void setup() {
        deBilt = new Station(260, "De Bilt");
        vlissingen = new Station(310, "Vlissingen");
    }


    @Test
    public void aStationHasAStringRepresentation() {
        assertEquals("260/De Bilt", deBilt.toString());
    }

    @Test
    public void canConvertATextLineToAStation() {
        Station station1 = Station.fromLine("235, De Kooy");
        Station station2 = Station.fromLine(" 380 , Maastricht , Nederland");
        Station station3 = Station.fromLine(" 999 ");
        Station station4 = Station.fromLine(" xxx, yyy ");

        assertEquals(235, station1.getStn());
        assertEquals("De Kooy", station1.getName());
        assertEquals(380, station2.getStn());
        assertEquals("Maastricht", station2.getName());
        assertEquals(null, station3);
    }

}