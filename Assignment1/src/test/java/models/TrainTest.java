package models;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TrainTest {
    Train passengerTrain, trainWithoutWagons, freightTrain;

    PassengerWagon passengerWagon1, passengerWagon2, passengerWagon3;
    FreightWagon freightWagon1, freightWagon2;

    @BeforeEach
    private void setup() {
        Locomotive rembrandt = new Locomotive(24531, 7);
        passengerTrain = new Train(rembrandt, "Amsterdam", "Paris");
        passengerTrain.attachToRear(new PassengerWagon(8001,32));
        passengerTrain.attachToRear(new PassengerWagon(8002,32));
        passengerTrain.attachToRear(new PassengerWagon(8003,18));
        passengerTrain.attachToRear(new PassengerWagon(8004,44));
        passengerTrain.attachToRear(new PassengerWagon(8005,44));
        passengerTrain.attachToRear(new PassengerWagon(8006,44));
        passengerTrain.attachToRear(new PassengerWagon(8007,40));

        Locomotive vanGogh = new Locomotive(29123, 7);
        trainWithoutWagons = new Train(vanGogh, "Amsterdam", "London");

        Locomotive clusius = new Locomotive(63427, 50);
        freightTrain = new Train(clusius, "Amsterdam", "Berlin");
        freightTrain.attachToRear(new FreightWagon(9001,50000));
        freightTrain.attachToRear(new FreightWagon(9002,40000));
        freightTrain.attachToRear(new FreightWagon(9003,30000));

        passengerWagon1 = new PassengerWagon(8011,50);
        passengerWagon2 = new PassengerWagon(8012,50);
        passengerWagon3 = new PassengerWagon(8013,50);
        passengerWagon1.attachTail(passengerWagon2);
        passengerWagon2.attachTail(passengerWagon3);
        freightWagon1 = new FreightWagon(9011,60000);
        freightWagon2 = new FreightWagon(9012,60000);
        freightWagon1.attachTail(freightWagon2);
    }

    // TODO extra test representation invariant
    @AfterEach
    private void representationInvariant() {
        assertTrue(!passengerWagon1.hasNextWagon() || passengerWagon1 == passengerWagon1.getNextWagon().getPreviousWagon());
        assertTrue(!passengerWagon2.hasNextWagon() || passengerWagon2 == passengerWagon2.getNextWagon().getPreviousWagon());
        assertTrue(!passengerWagon3.hasNextWagon() || passengerWagon3 == passengerWagon3.getNextWagon().getPreviousWagon());
        assertTrue(!freightWagon1.hasNextWagon() || freightWagon1 == freightWagon1.getNextWagon().getPreviousWagon());
        assertTrue(!freightWagon2.hasNextWagon() || freightWagon2 == freightWagon2.getNextWagon().getPreviousWagon());

        for (Wagon wagon = passengerTrain.getFirstWagon(); wagon != null; wagon = wagon.getNextWagon()) {
            assertTrue(!wagon.hasPreviousWagon() || wagon == wagon.getPreviousWagon().getNextWagon());
        }
        for (Wagon wagon = freightTrain.getFirstWagon(); wagon != null; wagon = wagon.getNextWagon()) {
            assertTrue(!wagon.hasPreviousWagon() || wagon == wagon.getPreviousWagon().getNextWagon());
        }
    }

    @Test
    public void T11_APassengerTrainsIsNoAFreightTrain() {
        passengerTrain = new Train(new Locomotive(13, 13), "Here", "There");
        passengerTrain.setFirstWagon(new PassengerWagon(1313, 1300));
        assertTrue(passengerTrain.isPassengerTrain());
        assertFalse(passengerTrain.isFreightTrain());
    }

    @Test
    public void T11_AFreightTrainIsNotAPassengerTrain() {
        freightTrain = new Train(new Locomotive(13, 13), "Here", "There");
        freightTrain.setFirstWagon(new FreightWagon(1313, 1300));
        assertFalse(freightTrain.isPassengerTrain());
        assertTrue(freightTrain.isFreightTrain());
    }

    @Test
    public void T11_ATrainWithoutWagonsIsNotAPassengerOrAFreightTrain() {
        assertFalse(trainWithoutWagons.isPassengerTrain());
        assertFalse(trainWithoutWagons.isFreightTrain());
    }

    @Test
    public void T12_ATrainWithoutWagonsShouldBeEmpty() {
        assertFalse(trainWithoutWagons.hasWagons());
        assertEquals(0, trainWithoutWagons.getNumberOfWagons());
        assertNull(trainWithoutWagons.getLastWagonAttached());
    }

    @Test
    public void T12_ATrainWithSevenWagonsShouldReportThose() {
        assertTrue(passengerTrain.hasWagons());
        assertEquals(7, passengerTrain.getNumberOfWagons());
    }

    @Test
    public void T12_ATrainWithThreeWagonsShouldReportThose() {
        assertTrue(freightTrain.hasWagons());
        assertEquals(3, freightTrain.getNumberOfWagons());
    }

    @Test
    public void T13_checkCumulativeWagonPropertiesOnTrain() {
        assertEquals( 254, passengerTrain.getTotalNumberOfSeats());
        assertEquals( 0, trainWithoutWagons.getTotalNumberOfSeats());
        assertEquals( 0, freightTrain.getTotalNumberOfSeats());
        assertEquals( 0, passengerTrain.getTotalMaxWeight());
        assertEquals( 0, trainWithoutWagons.getTotalMaxWeight());
        assertEquals( 120000, freightTrain.getTotalMaxWeight());

        // check final wagon
        assertEquals( 40, ((PassengerWagon) passengerTrain.getLastWagonAttached()).getNumberOfSeats());
        assertEquals( 30000, ((FreightWagon) freightTrain.getLastWagonAttached()).getMaxWeight());
        System.out.println(passengerTrain);

        // check toString
        assertTrue(freightTrain.toString().indexOf(" from Amsterdam to Berlin") > 0);
    }

    @Test
    public void T14_findWagonOnTrainAtPosition() {

        // find by position
        assertEquals(8001, passengerTrain.findWagonAtPosition(1).getId());
        assertEquals(8002, passengerTrain.findWagonAtPosition(2).getId());
        assertEquals(8007, passengerTrain.findWagonAtPosition(7).getId());
        assertNull(passengerTrain.findWagonAtPosition(8));
        assertNull(passengerTrain.findWagonAtPosition(0));
        assertNull(trainWithoutWagons.findWagonAtPosition(1));
    }

    @Test
    public void T15_findWagonOnTrainById() {
        // find by id
        assertEquals(50000, ((FreightWagon)(freightTrain.findWagonById(9001))).getMaxWeight());
        assertEquals(40000, ((FreightWagon)(freightTrain.findWagonById(9002))).getMaxWeight());
        assertEquals(30000, ((FreightWagon)(freightTrain.findWagonById(9003))).getMaxWeight());
        assertNull(freightTrain.findWagonById(9000));
        assertNull(trainWithoutWagons.findWagonById(8000));
    }

    @Test
    public void T16_CantAttachMoreWagonsThanTrainsCapacity() {
        assertFalse(passengerTrain.attachToRear(passengerWagon1));
        assertFalse(passengerTrain.insertAtFront(passengerWagon1));
    }

    @Test
    public void T16_CantAttachPassengerWagonsToFreightTrain() {
        assertFalse(freightTrain.attachToRear(passengerWagon1));
        assertFalse(freightTrain.insertAtFront(passengerWagon1));
    }

    @Test
    public void T16_CantAttachFreightWagonsToPassengerTrain() {
        assertFalse(passengerTrain.attachToRear(freightWagon1));
        assertFalse(passengerTrain.insertAtFront(freightWagon1));
    }


    @Test
    public void T16_CanAttachToRearWhenTrainsHasCapacity() {
        assertTrue(freightTrain.attachToRear(freightWagon1));
        assertEquals(5, freightTrain.getNumberOfWagons());
    }

    @Test
    public void T16_CanInsertAtFront() {
        assertTrue(freightTrain.insertAtFront(freightWagon1));
        assertEquals(5, freightTrain.getNumberOfWagons());
    }

    @Test
    public void T16_CanInsertPassengerWagonsToEmptyTrainWithCapacity() {
        // check type compatibility and loc capacity
        assertTrue(trainWithoutWagons.insertAtFront(passengerWagon1));
        assertEquals(3, trainWithoutWagons.getNumberOfWagons());
    }

    @Test
    public void T16_CantInsertWagonAlreadyOnTrain() {
        // check type compatibility and loc capacity
        assertTrue(trainWithoutWagons.insertAtFront(passengerWagon1));
        assertEquals(3, trainWithoutWagons.getNumberOfWagons());
        assertFalse(trainWithoutWagons.insertAtFront(passengerWagon1));
        assertEquals(3, trainWithoutWagons.getNumberOfWagons());
    }

    @Test
    public void T16_CanInsertAtPositionOneInEmptyTrain() {
        assertTrue(trainWithoutWagons.insertAtPosition(1, passengerTrain.getLastWagonAttached()));
    }

    @Test
    public void T16_CantInsertAtPositionBeyondLastWagon() {
        assertFalse(trainWithoutWagons.insertAtPosition(2, passengerTrain.getLastWagonAttached()));
    }

    @Test
    public void T17_ShouldSplitTrainCorrectly() {
        assertTrue(passengerTrain.splitAtPosition(5, trainWithoutWagons));
        assertEquals(3, trainWithoutWagons.getNumberOfWagons());
        assertEquals(4, passengerTrain.getNumberOfWagons());
    }

    @Test
    public void T17_ShouldMoveWagonsCorrectly() {
        assertTrue(passengerTrain.splitAtPosition(5, trainWithoutWagons));
        assertFalse(trainWithoutWagons.moveOneWagon(8001, passengerTrain));
        assertTrue(trainWithoutWagons.moveOneWagon(8006, passengerTrain));
        assertEquals(2, trainWithoutWagons.getNumberOfWagons());
        assertEquals(5, passengerTrain.getNumberOfWagons());
        assertEquals(8006, passengerTrain.findWagonAtPosition(5).getId());
    }

    @Test
    public void T17_ShouldMoveFirstWagonCorrectly() {
        Train train = new Train(new Locomotive(13, 13), "Here", "There");
        passengerTrain = new Train(new Locomotive(13, 13),  "There", "Here");
        Wagon firstWagon = new PassengerWagon(13, 1313);
        Wagon lastWagon = new PassengerWagon(14, 1414);
        train.attachToRear(firstWagon);
        train.attachToRear(lastWagon);

        assertTrue(train.moveOneWagon(13, passengerTrain));

        assertEquals(1, train.getNumberOfWagons());
        assertEquals(lastWagon, train.getFirstWagon());
        assertEquals(1, passengerTrain.getNumberOfWagons());
        assertEquals(firstWagon, passengerTrain.getFirstWagon());
    }

    @Test
    public void T17_ShouldMoveLastWagonCorrectly() {
        Train train = new Train(new Locomotive(13, 13), "Here", "There");
        passengerTrain = new Train(new Locomotive(13, 13),  "There", "Here");
        Wagon firstWagon = new PassengerWagon(13, 1313);
        Wagon lastWagon = new PassengerWagon(14, 1414);
        train.attachToRear(firstWagon);
        train.attachToRear(lastWagon);

        assertTrue(train.moveOneWagon(14, passengerTrain));

        assertEquals(1, train.getNumberOfWagons());
        assertEquals(firstWagon, train.getFirstWagon());
        assertEquals(1, passengerTrain.getNumberOfWagons());
        assertEquals(lastWagon, passengerTrain.getFirstWagon());
    }

    @Test
    public void T17_ShouldNotMoveWagonWhenTrainsAreIncompatible() {
        assertFalse(passengerTrain.moveOneWagon(8006, freightTrain));
        assertEquals(7, passengerTrain.getNumberOfWagons());
        assertEquals(8006, passengerTrain.findWagonAtPosition(6).getId());
        assertEquals(3, freightTrain.getNumberOfWagons());
    }

    @Test
    public void T17_CantSplitAnEmptyTrain() {
        assertFalse(trainWithoutWagons.splitAtPosition(1, passengerTrain));
    }

    @Test
    public void T17_CanSplitTrainAtFirstWagon() {
        Train train = new Train(new Locomotive(13, 13), "Here", "There");
        PassengerWagon wagon = new PassengerWagon(13, 1313);
        train.attachToRear(wagon);
        passengerTrain = new Train(new Locomotive(31, 3131), "There", "Here");

        assertTrue(train.splitAtPosition(1, passengerTrain));

        assertFalse(train.hasWagons());
        assertEquals(1, passengerTrain.getNumberOfWagons());
        assertEquals(wagon, passengerTrain.getFirstWagon());
    }

    @Test
    public void T17_CanSplitTrainAtLastWagon() {
        Train train = new Train(new Locomotive(13, 13), "Here", "There");
        PassengerWagon firstWagon = new PassengerWagon(13, 1313);
        train.attachToRear(firstWagon);
        PassengerWagon lastWagon = new PassengerWagon(14, 1414);
        train.attachToRear(lastWagon);
        passengerTrain = new Train(new Locomotive(31, 3131), "There", "Here");

        assertTrue(train.splitAtPosition(2, passengerTrain));

        assertEquals(1, train.getNumberOfWagons());
        assertEquals(firstWagon, train.getFirstWagon());
        assertEquals(1, passengerTrain.getNumberOfWagons());
        assertEquals(lastWagon, passengerTrain.getFirstWagon());
    }

    @Test
    public void T18_checkReverseTrain() {

        // check type compatibility and loc capacity
        passengerTrain.reverse();
        assertEquals(7, passengerTrain.getNumberOfWagons());
        assertEquals(8007, passengerTrain.findWagonAtPosition(1).getId());
        assertEquals(8006, passengerTrain.findWagonAtPosition(2).getId());
        assertEquals(8005, passengerTrain.findWagonAtPosition(3).getId());
        assertEquals(8001, passengerTrain.findWagonAtPosition(7).getId());

        trainWithoutWagons.reverse();
        assertEquals(0, trainWithoutWagons.getNumberOfWagons());
    }
}
