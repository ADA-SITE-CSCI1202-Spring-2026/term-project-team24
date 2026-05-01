package model;

import java.util.HashMap;
import java.util.Map;

public class CargoFreighter extends Aircraft {

    private static final int CARTS_REQUIRED = 10;
    private static final double REVENUE = 5000.0;

    public CargoFreighter(String flightNumber) {
        super(flightNumber, 8000, 60);
    }

    @Override
    public Map<SupplyItem, Integer> getRequiredSupplies() {
        Map<SupplyItem, Integer> needs = new HashMap<>();
        needs.put(SupplyItem.FUEL, getFuelRequired());
        needs.put(SupplyItem.BAGGAGE_CARTS, CARTS_REQUIRED);
        return needs;
    }

    @Override public double getRevenue() {
        return REVENUE;
    }
    @Override public String getType() {
        return "CargoFreighter";
    }
}
