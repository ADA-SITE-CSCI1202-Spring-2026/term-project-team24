package model;

public class CargoFreighter extends Aircraft {
    public CargoFreighter(String flightNumber) {
        super(flightNumber, 5000, 0, 10000, 45);
    }
    @Override
    public String getType() {
        return "Cargo Freighter";
    }
}