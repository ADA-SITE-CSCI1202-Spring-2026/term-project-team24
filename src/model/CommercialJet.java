package model;

public class CommercialJet extends Aircraft {
    public CommercialJet(String flightNumber) {
        super(flightNumber, 8000, 60);
    }

    @Override
    public String getType() {
        return "Commercial Jet";
    }
}