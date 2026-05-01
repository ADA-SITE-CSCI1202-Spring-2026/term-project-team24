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


    public boolean hasEnough(int fuelNeeded, int mealsNeeded) {
        return getSupply(SupplyItem.FUEL) >= fuelNeeded &&
                getSupply(SupplyItem.MEALS) >= mealsNeeded;
    }


    public void consumeResources(int fuel, int meals, int revenue) {

        supplies.put(SupplyItem.FUEL, getSupply(SupplyItem.FUEL) - fuel);
        supplies.put(SupplyItem.MEALS, getSupply(SupplyItem.MEALS) - meals);

        budget += revenue;
    }


    public boolean purchase(SupplyItem item) {

        int cost = 5000;

        if (budget >= cost) {
            supplies.put(item, getSupply(item) + 1000);
            budget -= cost;
            return true;
        }

        return false;
    }


}