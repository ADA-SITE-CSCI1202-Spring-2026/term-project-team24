package service;

import model.*;

public class BaggageHandler implements IGroundService {

    @Override
    public boolean canProcess(Aircraft aircraft) {
        return aircraft.getRequiredSupplies().containsKey(SupplyItem.BAGGAGE_CARTS);
    }

    @Override
    public void service(Aircraft aircraft, Logger logger) {
        int carts = aircraft.getRequiredSupplies().getOrDefault(SupplyItem.BAGGAGE_CARTS, 0);
        logger.log("Baggage Handler deployed " + carts + " carts for " + aircraft.getFlightNumber());
    }
}