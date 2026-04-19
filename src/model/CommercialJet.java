package model;

public class CommercialJet extends Aircraft {
    public CommercialJet(String flightNumber, int fuelRequired, int turnaroundTime) {
        super(flightNumber, fuelRequired, turnaroundTime);
    }

    @Override
    public String getType() {
        return "Commercial Jet";
    }
}