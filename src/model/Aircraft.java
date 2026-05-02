package model;

import java.util.Map;

public abstract class Aircraft {

    private String flightNumber;
    private int fuelRequired;
    private int turnaroundTime;

    public Aircraft(String flightNumber, int fuelRequired, int turnaroundTime) {
        this.flightNumber = flightNumber;
        this.fuelRequired = fuelRequired;
        this.turnaroundTime = turnaroundTime;
    }

    public abstract Map<SupplyItem, Integer> getRequiredSupplies();
    public abstract double getRevenue();
    public abstract String getType();

    public String getFlightNumber() {
        return flightNumber;
    }
    public int getFuelRequired() {
        return fuelRequired;
    }
    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    @Override
    public String toString() {
        return "[" + getType() + "] " + flightNumber;
    }
    public abstract String getType();
}