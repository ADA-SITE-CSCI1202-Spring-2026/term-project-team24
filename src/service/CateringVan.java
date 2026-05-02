package service;

import model.*;

public class CateringVan implements IGroundService {

    @Override
    public boolean canProcess(Aircraft aircraft) {
        return aircraft.getRequiredSupplies().containsKey(SupplyItem.MEALS);
    }

    @Override
    public void service(Aircraft aircraft, Logger logger) {
        int meals = aircraft.getRequiredSupplies().getOrDefault(SupplyItem.MEALS, 0);
        logger.log("Catering Van loaded " + meals + " meals onto " + aircraft.getFlightNumber());
    }
}
}
}
