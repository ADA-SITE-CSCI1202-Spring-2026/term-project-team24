package model;

public class CargoFreighter extends Aircraft {
    public CargoFreighter(String flightNumber, int fuelRequired, int turnaroundTime) {
        super(flightNumber, fuelRequired, turnaroundTime);
    }

    @Override
    public String getType() {
        return "Cargo Freighter";
    }
}