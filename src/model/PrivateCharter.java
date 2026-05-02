package model;

import java.util.HashMap;
import java.util.Map;

public class PrivateCharter extends Aircraft {

    private static final int MEALS_REQUIRED = 20;
    private static final double REVENUE = 2500.0;

    public PrivateCharter(String flightNumber) {
        super(flightNumber, 1500, 20);
    }

    @Override
    public Map<SupplyItem, Integer> getRequiredSupplies() {
        Map<SupplyItem, Integer> needs = new HashMap<>();
        needs.put(SupplyItem.FUEL,  getFuelRequired());
        needs.put(SupplyItem.MEALS, MEALS_REQUIRED);
        return needs;
    }

    @Override public double getRevenue() {
        return REVENUE;
    }
    @Override public String getType() {
        return "PrivateCharter";
    }
}
