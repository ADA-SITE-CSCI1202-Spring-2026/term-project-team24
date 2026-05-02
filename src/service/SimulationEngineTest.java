package service;

import model.*;
import java.util.*;

public class SimulationEngineTest {

    public static void main(String[] args) {
        System.out.println("SimulationEngine Test\n");

        DepotManager depot = new DepotManager();
        SimulationEngine engine = new SimulationEngine(depot);

        engine.setOnLog(msg -> System.out.println("[LOG] " + msg));
        engine.setOnQueueUpdated(() -> System.out.println("[QUEUE UPDATED] size: " + engine.getQueueSnapshot().size()));
        engine.setOnStateUpdated(() -> System.out.println("[STATE UPDATED] budget: $" + depot.getBudget()));

        //Test 1: empty queue
        System.out.println("--- Test 1: Process on empty queue");
        engine.processNextAircraft();
        System.out.println();

        System.out.println("--- Test 2: Generate 3 aircraft manually");
        depot.enqueue(new CommercialJet("FL-101"));
        depot.enqueue(new CargoFreighter("FL-102"));
        depot.enqueue(new PrivateCharter("FL-103"));
        System.out.println("Queue size: " + engine.getQueueSnapshot().size());
        System.out.println();

        System.out.println("--- Test 3: Process CommercialJet (should succeed)");
        System.out.println("Budget before: $" + depot.getBudget());
        System.out.println("Fuel before:   " + depot.getSupplyAmount(SupplyItem.FUEL));
        System.out.println("Meals before:  " + depot.getSupplyAmount(SupplyItem.MEALS));
        System.out.println("Carts before:  " + depot.getSupplyAmount(SupplyItem.BAGGAGE_CARTS));
        engine.processNextAircraft();
        System.out.println("Budget after:  $" + depot.getBudget());
        System.out.println("Fuel after:    " + depot.getSupplyAmount(SupplyItem.FUEL));
        System.out.println("Meals after:   " + depot.getSupplyAmount(SupplyItem.MEALS));
        System.out.println("Carts after:   " + depot.getSupplyAmount(SupplyItem.BAGGAGE_CARTS));
        System.out.println();

        System.out.println("Test 4: Process CargoFreighter (should succeed)");
        engine.processNextAircraft();
        System.out.println("Fuel after:  " + depot.getSupplyAmount(SupplyItem.FUEL));
        System.out.println("Carts after: " + depot.getSupplyAmount(SupplyItem.BAGGAGE_CARTS));
        System.out.println();

        System.out.println("Test 5: Drain fuel, process PrivateCharter (should fail)");
        depot.setSupply(SupplyItem.FUEL, 0);
        System.out.println("Fuel set to: " + depot.getSupplyAmount(SupplyItem.FUEL));
        engine.processNextAircraft();
        System.out.println("Queue size after failed process: " + engine.getQueueSnapshot().size());
        System.out.println();

        System.out.println("Test 6: Restock fuel, retry PrivateCharter (should succeed)");
        engine.purchaseSupply(SupplyItem.FUEL);
        System.out.println("Fuel after restock: " + depot.getSupplyAmount(SupplyItem.FUEL));
        engine.processNextAircraft();
        System.out.println("Queue size after success: " + engine.getQueueSnapshot().size());
        System.out.println();

        System.out.println("Test 7: Set budget to 0, try to restock (should fail)");
        depot.setBudget(0);
        engine.purchaseSupply(SupplyItem.MEALS);
        System.out.println();

        System.out.println("Test 8: Consume more than available (no negatives)");
        depot.setSupply(SupplyItem.FUEL, 10);
        Map<SupplyItem, Integer> bigDemand = new HashMap<>();
        bigDemand.put(SupplyItem.FUEL, 9999);
        depot.consumeSupplies(bigDemand);
        System.out.println("Fuel after over-consume: " + depot.getSupplyAmount(SupplyItem.FUEL) + " (should be 0)");
        System.out.println();

        System.out.println("All Tests Done");
    }
}
