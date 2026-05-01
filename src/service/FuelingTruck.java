package service;

import model.Aircraft;

public class FuelingTruck implements IGroundService{
    @Override
    public boolean canProcess(Aircraft aircraft){
        return aircraft.getFuelRequired()>0;
    }

    @Override
    public void serviceFlight(Aircraft aircraft){
        System.out.println("Pumping " + aircraft.getFuelRequired() + " L into " + aircraft.getFlightNumber());
    }
}
}
