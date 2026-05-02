package service;

import model.*;

public class FuelingTruck implements IGroundService {

    @Override
    public boolean canProcess(Aircraft aircraft) {
        return aircraft.getRequiredSupplies().containsKey(SupplyItem.FUEL);
    }

    @Override
    public void service(Aircraft aircraft, Logger logger) {
        int fuel = aircraft.getRequiredSupplies().getOrDefault(SupplyItem.FUEL, 0);
        logger.log("Fueling Truck dispensed " + fuel + "L to " + aircraft.getFlightNumber());
    }
}
}
}
