package service;

import model.Aircraft;

public class BaggageHandler implements IGroundService{
    @Override
    public boolean canProcess(Aircraft aircraft){
        return true;
    }

     @Override
    public void serviceFlight(Aircraft aircraft) {
        System.out.println("Handling luggage for " + aircraft.getFlightNumber());
    }
}
