package service;

import model.*;

public class BaggageHandler implements IGroundService {

    @Override
    public boolean canProcess(Aircraft aircraft) {
        return aircraft.getRequiredSupplies().containsKey(SupplyItem.BAGGAGE_CARTS);
    }

    @Override
    public boolean service(Aircraft aircraft, DepotManager depot) {

        int cartsNeeded = aircraft.getRequiredSupplies().get(SupplyItem.BAGGAGE_CARTS);

        if (!depot.hasEnough(SupplyItem.BAGGAGE_CARTS, cartsNeeded)) return false;

        depot.consumeResources(SupplyItem.BAGGAGE_CARTS, cartsNeeded);
        return true;
    public void serviceFlight(Aircraft aircraft) {
        System.out.println("Handling luggage for " + aircraft.getFlightNumber());
    }
}
     @Override
    public void serviceFlight(Aircraft aircraft) {
        System.out.println("Handling luggage for " + aircraft.getFlightNumber());
    }
}