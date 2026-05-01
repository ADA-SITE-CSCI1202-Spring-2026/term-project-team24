package service;

import model.*;

public class FuelingTruck implements IGroundService {

    @Override
    public boolean canProcess(Aircraft aircraft) {
        return aircraft.getRequiredSupplies().containsKey(SupplyItem.FUEL);
    }

    @Override
    public boolean service(Aircraft aircraft, DepotManager depot) {

        int fuelNeeded = aircraft.getRequiredSupplies().get(SupplyItem.FUEL);

        if (!depot.hasEnough(SupplyItem.FUEL, fuelNeeded)) return false;

        depot.consumeResources(SupplyItem.FUEL, fuelNeeded);
        return true;
    }
}
}
}
