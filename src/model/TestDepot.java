package model;

import java.util.EnumMap;
import java.util.Map;

public class TestDepot {

    public static void main(String[] args) {

        DepotManager depot = new DepotManager();

        System.out.println("Fuel: " + depot.getSupplyAmount(SupplyItem.FUEL));

        Map<SupplyItem, Integer> required = new EnumMap<>(SupplyItem.class);
        required.put(SupplyItem.FUEL, 1000);
        required.put(SupplyItem.MEALS, 200);
        boolean ok = depot.hasSufficientSupplies(required);
        System.out.println("Can process: " + ok);

        depot.consumeSupplies(required);
        depot.addRevenue(10000);

        System.out.println("Fuel after: " + depot.getSupplyAmount(SupplyItem.FUEL));
        System.out.println("Budget: " + depot.getBudget());
    }
}
