package models;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class WagonTest {

    Wagon passengerWagon1, passengerWagon2, passengerWagon3, passengerWagon4;
    Wagon freightWagon1, freightWagon2;

    @BeforeEach
    private void setup() {
        passengerWagon1 = new PassengerWagon(8001, 36);
        passengerWagon2 = new PassengerWagon(8002, 18);
        passengerWagon3 = new PassengerWagon(8003, 48);
        passengerWagon4 = new PassengerWagon(8004, 44);
        freightWagon1 = new FreightWagon(9001, 50000);
        freightWagon2 = new FreightWagon(9002, 60000);
    }

    @AfterEach
    private void representationInvariant() {
        assertTrue(!passengerWagon1.hasNextWagon() || passengerWagon1 == passengerWagon1.getNextWagon().getPreviousWagon());
        assertTrue(!passengerWagon2.hasNextWagon() || passengerWagon2 == passengerWagon2.getNextWagon().getPreviousWagon());
        assertTrue(!passengerWagon3.hasNextWagon() || passengerWagon3 == passengerWagon3.getNextWagon().getPreviousWagon());
        assertTrue(!passengerWagon4.hasNextWagon() || passengerWagon4 == passengerWagon4.getNextWagon().getPreviousWagon());
    }

    @Test
    public void T01_AWagonCannotBeInstantiated() {
        // Dig deep ;-)
        assertTrue((Wagon.class.getModifiers() & 0x00000400) != 0);
    }

    @Test
    public void T02_APassengerWagonShouldReportCorrectProperties() {
        // check subclasses
        assertFalse(passengerWagon1 instanceof FreightWagon);

        // check properties
        assertEquals(8001, passengerWagon1.getId());
        assertEquals(36, ((PassengerWagon) passengerWagon1).getNumberOfSeats());

        // check printed information
        assertEquals("[Wagon-8001]", passengerWagon1.toString());
    }

    @Test
    public void T02_AFreightWagonShouldReportCorrectProperties() {
        // check subclasses
        assertFalse(freightWagon1 instanceof PassengerWagon);

        // check properties
        assertEquals(9001, freightWagon1.getId());
        assertEquals(50000, ((FreightWagon) freightWagon1).getMaxWeight());

        // check printed information
        assertEquals("[Wagon-9001]", freightWagon1.toString());
    }


    @Test
    public void T03_ASingleWagonShouldHaveATailLengthOfZero() {
        assertEquals(0, passengerWagon1.getTailLength(),
                "A single wagon should have a tail of length=0");
    }

    @Test
    public void T03_ASingleWagonIsTheLastWagonOfASequence() {
        assertEquals(passengerWagon1, passengerWagon1.getLastWagonAttached(),
                "A single wagon should be the last wagon of its own sequence");
    }

    @Test
    public void T03_AttachTailCanOnlyConnectHeadWagons() {
        passengerWagon1.attachTail(passengerWagon2);
        Throwable t;

        t = assertThrows(IllegalStateException.class,
                () -> { passengerWagon1.attachTail(passengerWagon2);}
        );
        assertTrue(t.getMessage().contains(passengerWagon1.toString()),
                "Exception message should include the names of connected wagons");
        assertTrue(t.getMessage().contains(passengerWagon2.toString()),
                "Exception message should include the names of connected wagons");

        t = assertThrows(IllegalStateException.class,
                () -> { passengerWagon3.attachTail(passengerWagon2);}
        );
        assertTrue(t.getMessage().contains(passengerWagon1.toString()),
                "Exception message should include the names of connected wagons");
        assertTrue(t.getMessage().contains(passengerWagon2.toString()),
                "Exception message should include the names of connected wagons");
    }

    @Test
    public void T03_TheFirstOfFourWagonsShouldReportATailLengthOfThree() {
        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon2.attachTail(passengerWagon3);
        passengerWagon3.attachTail(passengerWagon4);

        assertEquals(3, passengerWagon1.getTailLength(),
                "After three attachments a wagon's tail should have length=3");
    }

    @Test
    public void T03_TheFirstWagonOfFourWagonsShouldReturnTheLastWagonOfTheSequence() {
        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon2.attachTail(passengerWagon3);
        passengerWagon3.attachTail(passengerWagon4);

        assertEquals(passengerWagon4, passengerWagon1.getLastWagonAttached(),
                "The last attachment should become the last wagon in a sequence");
    }

    @Test
    public void T03_TheSecondLastWagonShouldReportATailLengthOfOne() {
        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon2.attachTail(passengerWagon3);
        passengerWagon3.attachTail(passengerWagon4);

        assertEquals(1, passengerWagon3.getTailLength());
    }

    @Test
    public void T03_TheSecondLastWagonShouldReturnTheLastOfTheSequence() {
        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon2.attachTail(passengerWagon3);
        passengerWagon3.attachTail(passengerWagon4);

        assertEquals(passengerWagon4, passengerWagon3.getLastWagonAttached());
    }

    @Test
    public void T03_TheLastWagonShouldReportATailLengthOfZero() {
        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon2.attachTail(passengerWagon3);
        passengerWagon3.attachTail(passengerWagon4);

        assertEquals(0, passengerWagon4.getTailLength());
    }

    @Test
    public void T03_TheLastWagonShouldReturnTheLastOfTheSequence() {
        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon2.attachTail(passengerWagon3);
        passengerWagon3.attachTail(passengerWagon4);

        assertEquals(passengerWagon4, passengerWagon4.getLastWagonAttached());
    }

    @Test
    public void T04_AttachingFourWagonsShouldResultInSequenceOfFour() {
        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon2.attachTail(passengerWagon3);
        passengerWagon3.attachTail(passengerWagon4);

        assertEquals(passengerWagon2, passengerWagon1.getNextWagon());
        assertFalse(passengerWagon1.hasPreviousWagon());

        assertEquals(passengerWagon3, passengerWagon2.getNextWagon());
        assertEquals(passengerWagon1, passengerWagon2.getPreviousWagon());

        assertEquals(passengerWagon4, passengerWagon3.getNextWagon());
        assertEquals(passengerWagon2, passengerWagon3.getPreviousWagon());

        assertFalse(passengerWagon4.hasNextWagon());
        assertEquals(passengerWagon3, passengerWagon4.getPreviousWagon());
    }

    @Test
    public void T04_DetachingThirdWagonFromSequenceOfFourShouldResultInTwoSequences() {
        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon2.attachTail(passengerWagon3);
        passengerWagon3.attachTail(passengerWagon4);

        Wagon oldFront = passengerWagon3.detachFront();

        assertEquals(passengerWagon2, oldFront);

        assertEquals(passengerWagon2, passengerWagon1.getNextWagon());
        assertFalse(passengerWagon1.hasPreviousWagon());

        assertFalse(passengerWagon2.hasNextWagon());
        assertEquals(passengerWagon1, passengerWagon2.getPreviousWagon());

        assertEquals(passengerWagon4, passengerWagon3.getNextWagon());
        assertFalse(passengerWagon3.hasPreviousWagon());

        assertFalse(passengerWagon4.hasNextWagon());
        assertEquals(passengerWagon3, passengerWagon4.getPreviousWagon());
    }

    @Test
    public void T04_ReAttachShouldMoveWagonToNewSequence() {
        // Two separate sequences!
        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon3.attachTail(passengerWagon4);

        passengerWagon4.reAttachTo(passengerWagon2);

        assertFalse(passengerWagon3.hasNextWagon());
        assertFalse(passengerWagon3.hasPreviousWagon());

        assertEquals(passengerWagon2, passengerWagon1.getNextWagon());
        assertFalse(passengerWagon1.hasPreviousWagon());

        assertEquals(passengerWagon4, passengerWagon2.getNextWagon());
        assertEquals(passengerWagon1, passengerWagon2.getPreviousWagon());

        assertFalse(passengerWagon4.hasNextWagon());
        assertEquals(passengerWagon2, passengerWagon4.getPreviousWagon());
    }

    @Test
    public void T04_DetatchInMiddleOfSequenceShouldResultInTwoSequences() {
        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon2.attachTail(passengerWagon3);
        passengerWagon3.attachTail(passengerWagon4);

        Wagon oldTail2 = passengerWagon2.detachTail();
        assertEquals(passengerWagon3, oldTail2);

        assertEquals(passengerWagon2, passengerWagon1.getNextWagon());
        assertFalse(passengerWagon1.hasPreviousWagon());

        assertFalse(passengerWagon2.hasNextWagon());
        assertEquals(passengerWagon1, passengerWagon2.getPreviousWagon());

        assertEquals(passengerWagon4, passengerWagon3.getNextWagon());
        assertFalse(passengerWagon3.hasPreviousWagon());

        assertFalse(passengerWagon4.hasNextWagon());
        assertEquals(passengerWagon3, passengerWagon4.getPreviousWagon());

        // repeated detachTail should return null;
        oldTail2 = passengerWagon2.detachTail();
        assertNull(oldTail2);
    }

    @Test
    public void T04_RemoveFirstWagonFromThreeShouldResultInSequenceOfTwo() {
        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon2.attachTail(passengerWagon3);

        // remove middle wagon
        passengerWagon1.removeFromSequence();

        assertFalse(passengerWagon1.hasNextWagon());
        assertFalse(passengerWagon1.hasPreviousWagon());

        assertEquals(passengerWagon3, passengerWagon2.getNextWagon());
        assertFalse(passengerWagon2.hasPreviousWagon());

        assertFalse(passengerWagon3.hasNextWagon());
        assertEquals(passengerWagon2, passengerWagon3.getPreviousWagon());
    }

    @Test
    public void T04_RemoveMiddleWagonFromThreeShouldResultInSequenceOfTwo() {
        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon2.attachTail(passengerWagon3);

        // remove middle wagon
        passengerWagon2.removeFromSequence();

        assertFalse(passengerWagon2.hasNextWagon());
        assertFalse(passengerWagon2.hasPreviousWagon());

        assertEquals(passengerWagon3, passengerWagon1.getNextWagon());
        assertFalse(passengerWagon1.hasPreviousWagon());

        assertFalse(passengerWagon3.hasNextWagon());
        assertEquals(passengerWagon1, passengerWagon3.getPreviousWagon());
    }

    @Test
    public void T04_RemoveLastWagonFromThreeShouldResultInSequenceOfTwo() {
        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon2.attachTail(passengerWagon3);

        // remove final wagon
        passengerWagon3.removeFromSequence();

        assertFalse(passengerWagon3.hasNextWagon());
        assertFalse(passengerWagon3.hasPreviousWagon());

        assertEquals(passengerWagon2, passengerWagon1.getNextWagon());
        assertFalse(passengerWagon1.hasPreviousWagon());

        assertFalse(passengerWagon2.hasNextWagon());
        assertEquals(passengerWagon1, passengerWagon2.getPreviousWagon());
    }

    @Test
    public void T05_WholeSequenceOfFourShouldBeReversed() {
        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon2.attachTail(passengerWagon3);
        passengerWagon3.attachTail(passengerWagon4);

        // reverse full sequence
        Wagon rev = passengerWagon1.reverseSequence();

        assertEquals(3, rev.getTailLength());
        assertEquals(passengerWagon4, rev);
        assertEquals(passengerWagon3, rev.getNextWagon());
        assertFalse(rev.hasPreviousWagon());

        assertEquals(passengerWagon2, passengerWagon3.getNextWagon());
        assertEquals(passengerWagon4, passengerWagon3.getPreviousWagon());

        assertEquals(passengerWagon1, passengerWagon2.getNextWagon());
        assertEquals(passengerWagon3, passengerWagon2.getPreviousWagon());

        assertFalse(passengerWagon1.hasNextWagon());
        assertEquals(passengerWagon2, passengerWagon1.getPreviousWagon());
    }

    @Test
    public void T05_PartiallyReverseASequenceOfFour() {
        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon2.attachTail(passengerWagon3);
        passengerWagon3.attachTail(passengerWagon4);

        // reverse part of the sequence
        Wagon rev = passengerWagon3.reverseSequence();
        assertEquals(1, rev.getTailLength());
        assertEquals(passengerWagon4, rev);

        assertEquals(passengerWagon3, rev.getNextWagon());
        assertEquals(passengerWagon2, rev.getPreviousWagon());

        assertFalse(passengerWagon3.hasNextWagon());
        assertEquals(passengerWagon4, passengerWagon3.getPreviousWagon());

        assertEquals(3, passengerWagon1.getTailLength());
        assertFalse(passengerWagon1.hasPreviousWagon());
        assertEquals(passengerWagon2, passengerWagon1.getNextWagon());

        assertEquals(passengerWagon1, passengerWagon2.getPreviousWagon());
        assertEquals(passengerWagon4, passengerWagon2.getNextWagon());
    }
}
