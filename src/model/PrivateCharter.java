package model;

public class PrivateCharter extends Aircraft {
    public PrivateCharter(String flightNumber, int fuelRequired, int turnaroundTime) {
        super(flightNumber, fuelRequired, turnaroundTime);
    }

    @Override
    public String getType() {
        return "Private Charter";
    }
}