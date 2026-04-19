package model;

public class CommercialJet extends Aircraft {
    public CommercialJet(String flightNumber) {
        super(flightNumber, 2000, 45);
    }

    @Override
    public String getType() {
        return "Commercial Jet";
    }
}