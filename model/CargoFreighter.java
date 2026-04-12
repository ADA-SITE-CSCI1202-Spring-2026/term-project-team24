package model;

public class CargoFreighter extends Aircraft {
    public CargoFreighter(String flightNumber) {
        super(flightNumber, 3000, 90);
    }

    @Override
    public String getType() {
        return "Cargo Freighter";
    }
}