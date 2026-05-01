package model;

import java.util.HashMap;
import java.util.Map;

public class CommercialJet extends Aircraft {
    private static final int MEALS_REQUIRED = 200;
    private static final int CARTS_REQUIRED = 5;
    private static final double REVENUE = 8000.0;

    public CommercialJet(String flightNumber) {
        super(flightNumber, 5000, 45);
    }

@Override
    public Map<SupplyItem, Integer> getRequiredSupplies(){
        Map<SupplyItem, Integer> needs = new HashMap<>();
        needs.put(SupplyItem.FUEL, getFuelRequired());
        needs.put(SupplyItem.MEALS, MEALS_REQUIRED);
        needs.put(SupplyItem.BAGGAGE_CARTS, CARTS_REQUIRED);
        return needs;
    }

    @Override public double getRevenue(){
        return REVENUE;
    }
    @Override public String getType(){
        return "CommercialJet";
    }
}