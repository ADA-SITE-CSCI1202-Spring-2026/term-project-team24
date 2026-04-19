package model;

public class PrivateCharter extends Aircraft {
    public PrivateCharter(String flightNumber) {
        super(flightNumber, 500, 10);
    }

    @Override
    public String getType() {
        return "Private Charter";
    }
}
