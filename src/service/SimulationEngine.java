package service;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import model.*;
import java.util.*;

public class SimulationEngine {

    private final DepotManager depotManager;
    private final List<IGroundService> services;
    private final Random random;
    private Timeline timer;
    private int flightCounter = 100;

    private Runnable onQueueUpdated;
    private Runnable onStateUpdated;
    private IGroundService.Logger onLog;

    public SimulationEngine(DepotManager depotManager) {
        this.depotManager = depotManager;
        this.random = new Random();
        this.services = new ArrayList<>();
        services.add(new FuelingTruck());
        services.add(new CateringVan());
        services.add(new BaggageHandler());
    }

    public void setOnQueueUpdated(Runnable r){
        this.onQueueUpdated = r;
    }
    public void setOnStateUpdated(Runnable r) {
        this.onStateUpdated = r;
    }
    public void setOnLog(IGroundService.Logger cb) {
        this.onLog = cb;
    }

    public void start() {
        timer = new Timeline(new KeyFrame(Duration.seconds(3), e -> addAircraftToQueue()));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
        log("Skyways dispatch is LIVE — new flights every 3s");
    }

    public void stop() {
        if (timer != null) timer.stop();
    }

    private String generateFlightNumber() {
        return "FL-" + (flightCounter++);
    }

    private Aircraft generateRandomAircraft() {
        switch (random.nextInt(3)) {
            case 0:  return new CommercialJet(generateFlightNumber());
            case 1:  return new CargoFreighter(generateFlightNumber());
            default: return new PrivateCharter(generateFlightNumber());
        }
    }

    private void addAircraftToQueue() {
        Aircraft aircraft = generateRandomAircraft();
        depotManager.enqueue(aircraft);
        log("WARNING: New Arrival — " + aircraft);
        if (onQueueUpdated != null) onQueueUpdated.run();
    }

    public void processNextAircraft() {
        Aircraft aircraft = depotManager.pollQueue();

        if (aircraft == null) {
            log("Queue is empty!");
            return;
        }

        Map<SupplyItem, Integer> needed = aircraft.getRequiredSupplies();

        if (!depotManager.hasSufficientSupplies(needed)) {
            StringBuilder missing = new StringBuilder();
            for (Map.Entry<SupplyItem, Integer> e : needed.entrySet()) {
                int have = depotManager.getSupplyAmount(e.getKey());
                if (have < e.getValue()) {
                    missing.append(e.getKey().getDisplayName())
                           .append(" (need ").append(e.getValue())
                           .append(", have ").append(have).append(") ");
                }
            }
            log("ERROR: Cannot clear " + aircraft.getFlightNumber()
                    + " — Insufficient: " + missing.toString().trim());
            depotManager.requeueFront(aircraft);
            if (onQueueUpdated != null) onQueueUpdated.run();
            return;
        }

        depotManager.consumeSupplies(needed);
        depotManager.addRevenue(aircraft.getRevenue());
        log("Cleared " + aircraft.getFlightNumber()
                + " — Revenue +$" + String.format("%.0f", aircraft.getRevenue()));

        IGroundService.Logger logCb = message -> log(message);
        for (IGroundService service : services) {
            if (service.canProcess(aircraft)) {
                service.serviceFlight(aircraft, logCb);
            }
        }

        if (onQueueUpdated != null) onQueueUpdated.run();
        if (onStateUpdated != null) onStateUpdated.run();
    }

    public void purchaseSupply(SupplyItem item) {
        boolean success = depotManager.restockSupply(item);
        if (success) {
            log("Purchased " + item.getDisplayName()
                    + " — new stock: " + depotManager.getSupplyAmount(item)
                    + " " + item.getUnit());
        } else {
            log("ERROR: Not enough budget to purchase " + item.getDisplayName());
        }
        if (onStateUpdated != null) onStateUpdated.run();
    }

    public List<Aircraft> getQueueSnapshot() {
        return depotManager.getQueueSnapshot();
    }

    private void log(String msg) {
        if (onLog != null) onLog.log(msg);
    }
}
