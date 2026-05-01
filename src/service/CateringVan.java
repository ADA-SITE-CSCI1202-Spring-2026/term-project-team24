package service;

import model.Aircraft;

public class CateringVan implements IGroundService {
    @Override
    public boolean canProcess(Aircraft aircraft) {
        return aircraft.getTurnaroundTime() > 15;
    }

    @Override
    public void serviceFlight(Aircraft aircraft) {
        System.out.println("Loading meal carts for " + aircraft.getFlightNumber());
    }
}
}
