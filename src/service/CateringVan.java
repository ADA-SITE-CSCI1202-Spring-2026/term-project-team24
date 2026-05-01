package service;

import model.*;

public class CateringVan implements IGroundService {

    @Override
    public boolean canProcess(Aircraft aircraft) {
        return aircraft.getRequiredSupplies().containsKey(SupplyItem.MEALS);
    }

    @Override
    public boolean service(Aircraft aircraft, DepotManager depot) {

        int mealsNeeded = aircraft.getRequiredSupplies().get(SupplyItem.MEALS);

        if (!depot.hasEnough(SupplyItem.MEALS, mealsNeeded)) return false;

        depot.consumeResources(SupplyItem.MEALS, mealsNeeded);
        return true;
    }
}
}
}
