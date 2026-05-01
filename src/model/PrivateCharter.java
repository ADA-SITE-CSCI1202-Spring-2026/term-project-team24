package model;

public class PrivateCharter extends Aircraft {
    public PrivateCharter(String flightNumber) {
        super(flightNumber, 1500, 50, 5000, 30);
    }
    @Override
    public String getType() {
        return "Private Charter";
    }
}