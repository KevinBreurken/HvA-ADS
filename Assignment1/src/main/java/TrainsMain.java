import models.*;

public class TrainsMain {

    public static void main(String[] args) {

        System.out.println("Welcome to the HvA trains configurator");

        Locomotive rembrandt = new Locomotive(24531, 200);
        Train amsterdamParis = new Train(rembrandt, "Amsterdam", "Paris");
        System.out.println(amsterdamParis.hasWagons());;

        amsterdamParis.attachToRear(new PassengerWagon(8001,32));

        amsterdamParis.attachToRear(new PassengerWagon(8002,32));
        amsterdamParis.attachToRear(new PassengerWagon(8003,18));
        amsterdamParis.attachToRear(new PassengerWagon(8004,44));

        amsterdamParis.attachToRear(new PassengerWagon(8005,44)); //4
        amsterdamParis.attachToRear(new PassengerWagon(8006,44));
        amsterdamParis.attachToRear(new PassengerWagon(8007,44));
        System.out.println(amsterdamParis);


        Locomotive vanGogh = new Locomotive(63427, 6);
        Train amsterdamLondon = new Train(vanGogh, "Amsterdam", "London");
        amsterdamLondon.attachToRear(new PassengerWagon(907,44));
        amsterdamLondon.attachToRear(new PassengerWagon(906,44));

        System.out.println(amsterdamParis.insertAtFront(amsterdamLondon.getFirstWagon()));
        System.out.println(amsterdamParis);
        //my code
//        System.out.println(amsterdamParis.canAttach(new FreightWagon(293, 1200)));
//        System.out.println(amsterdamParis.canAttach(new PassengerWagon(1200,12)));

//        System.out.println("Total number of seats: " + amsterdamParis.getTotalNumberOfSeats());
//        System.out.println("\nConfigurator result:");
//
//
//        amsterdamParis.splitAtPosition(4, amsterdamLondon);
//
//        //my code
//        System.out.println(amsterdamParis);
//        System.out.println(amsterdamLondon);

//        amsterdamLondon.reverse();
//        amsterdamLondon.insertAtFront(new FreightWagon(9001, 50000));
//        amsterdamParis.reverse();
//        amsterdamParis.splitAtPosition(1, amsterdamLondon);
//        amsterdamParis.attachToRear(amsterdamLondon.getLastWagonAttached());
//        amsterdamLondon.moveOneWagon(8003, amsterdamParis);
//
//        System.out.println(amsterdamParis);
//        System.out.println("Total number of seats: " + amsterdamParis.getTotalNumberOfSeats());
//        System.out.println(amsterdamLondon);
//        System.out.println("Total number of seats: " + amsterdamLondon.getTotalNumberOfSeats());
    }
}
