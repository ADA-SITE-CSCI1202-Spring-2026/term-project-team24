package model;

import java.util.HashMap;

public class DepotManager {

    private HashMap<SupplyItem, Integer> supplies;
    private int budget;

    public DepotManager() {
        supplies = new HashMap<>();

        supplies.put(SupplyItem.FUEL, 5000);
        supplies.put(SupplyItem.MEALS, 400);
        supplies.put(SupplyItem.BAGGAGE_CARTS, 15);

        budget = 50000;
    }

    public int getSupply(SupplyItem item) {
        return supplies.getOrDefault(item, 0);
    }

    public int getBudget() {
        return budget;
    }

    public boolean hasEnough(SupplyItem item, int amount) {
        return getSupply(item) >= amount;
    }

    public boolean consumeResources(SupplyItem item, int amount) {

        if (!hasEnough(item, amount)) {
            return false;
        }

        supplies.put(item, getSupply(item) - amount);
        return true;
    }

    public void addRevenue(double revenue) {
        budget += revenue;
    }

    public boolean purchase(SupplyItem item) {

        int cost = 5000;
        int restockAmount = 1000;

        if (budget < cost) return false;

        supplies.put(item, getSupply(item) + restockAmount);
        budget -= cost;
        return true;
    }
}