package model;

public class AircraftTest {
    public static void main(String[] args) {

        Aircraft jet = new CommercialJet("FL100");
        Aircraft cargo = new CargoFreighter("CG200");
        Aircraft privateJet = new PrivateCharter("PR300");

        Aircraft[] fleet = {jet, cargo, privateJet};

        for (Aircraft a : fleet) {
            System.out.println("Flight: " + a.getFlightNumber());
            System.out.println("Type: " + a.getType());
            System.out.println("Revenue: " + a.getRevenue());

            System.out.println("Required Supplies:");
            for (SupplyItem item : a.getRequiredSupplies().keySet()) {
                System.out.println(" - " + item + ": " + a.getRequiredSupplies().get(item));
            }
        }
    }
}
