package model;

import java.util.*;

public class DepotManager {

    private final HashMap<SupplyItem, Integer> supplies;
    private final Queue<Aircraft> flightQueue;
    private double budget;

    private static final Map<SupplyItem, Integer> RESTOCK_AMOUNT = new EnumMap<>(SupplyItem.class);
    private static final Map<SupplyItem, Double>  RESTOCK_COST   = new EnumMap<>(SupplyItem.class);

    static {
        RESTOCK_AMOUNT.put(SupplyItem.FUEL, 5000);
        RESTOCK_AMOUNT.put(SupplyItem.MEALS, 100);
        RESTOCK_AMOUNT.put(SupplyItem.BAGGAGE_CARTS, 10);

        RESTOCK_COST.put(SupplyItem.FUEL, 3000.0);
        RESTOCK_COST.put(SupplyItem.MEALS, 500.0);
        RESTOCK_COST.put(SupplyItem.BAGGAGE_CARTS, 200.0);
    }

    public DepotManager() {
        supplies = new HashMap<>();
        flightQueue = new ArrayDeque<>();
        budget = 50000.0;

        supplies.put(SupplyItem.FUEL, 5000);
        supplies.put(SupplyItem.MEALS, 400);
        supplies.put(SupplyItem.BAGGAGE_CARTS, 15);
    }

    public void enqueue(Aircraft aircraft) {
        flightQueue.add(aircraft);
    }
    public Aircraft pollQueue() {
        return flightQueue.poll();
    }
    public void requeueFront(Aircraft aircraft) {
        ((ArrayDeque<Aircraft>) flightQueue).addFirst(aircraft);
    }
    public List<Aircraft> getQueueSnapshot() {
        return new ArrayList<>(flightQueue);
    }

    public boolean hasSufficientSupplies(Map<SupplyItem, Integer> needed) {
        for (Map.Entry<SupplyItem, Integer> entry : needed.entrySet()) {
            if (getSupplyAmount(entry.getKey()) < entry.getValue()) return false;
        }
        return true;
    }

    public void consumeSupplies(Map<SupplyItem, Integer> needed) {
        for (Map.Entry<SupplyItem, Integer> entry : needed.entrySet()) {
            int current = getSupplyAmount(entry.getKey());
            supplies.put(entry.getKey(), Math.max(0, current - entry.getValue()));
        }
    }

    public boolean restockSupply(SupplyItem item) {
        double cost = RESTOCK_COST.getOrDefault(item, 0.0);
        if (budget < cost) return false;
        budget -= cost;
        supplies.put(item, getSupplyAmount(item) + RESTOCK_AMOUNT.getOrDefault(item, 0));
        return true;
    }

    public void addRevenue(double amount) {
        budget += amount; }

    public int getSupplyAmount(SupplyItem item) {
        return supplies.getOrDefault(item, 0);
    }
    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
    public void setSupply(SupplyItem item, int amount) { 
        supplies.put(item, Math.max(0, amount)); 
    }
    public void clearQueue() {
        flightQueue.clear();
    }
}