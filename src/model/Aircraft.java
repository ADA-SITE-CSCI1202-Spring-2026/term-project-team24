package model;

public abstract class Aircraft {

    private String flightNumber;
    private int fuelRequired;
    private int turnaroundTime;

    public Aircraft(String flightNumber, int fuel, int turnaroundTime) {
        this.flightNumber = flightNumber;
        this.fuelRequired = fuel;
        this.turnaroundTime = turnaroundTime;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public int getFuelRequired() {
        return fuelRequired;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }
    
    public abstract String getType();
}