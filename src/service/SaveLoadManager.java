package service;

import model.*;

import java.io.*;
import java.util.*;

public class SaveLoadManager {

    private static final String FILE_PATH = "airport_state.csv";

    public static boolean save(DepotManager depot) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {

            writer.write("BUDGET," + depot.getBudget());
            writer.newLine();

            for (SupplyItem item : SupplyItem.values()) {
                writer.write("SUPPLY," + item.name() + "," + depot.getSupplyAmount(item));
                writer.newLine();
            }

            for (Aircraft aircraft : depot.getQueueSnapshot()) {
                writer.write("QUEUE," + aircraft.getType() + "," + aircraft.getFlightNumber());
                writer.newLine();
            }

            return true;

        } catch (IOException e) {
            System.err.println("SaveLoadManager.save() failed: " + e.getMessage());
            return false;
        }
    }


    public static boolean load(DepotManager depot) {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.err.println("SaveLoadManager.load(): file not found — " + FILE_PATH);
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            depot.clearQueue();

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");

                switch (parts[0]) {

                    case "BUDGET":
                        depot.setBudget(Double.parseDouble(parts[1]));
                        break;

                    case "SUPPLY":
                        SupplyItem item = SupplyItem.valueOf(parts[1]);
                        depot.setSupply(item, Integer.parseInt(parts[2]));
                        break;

                    case "QUEUE":
                        Aircraft aircraft = reconstructAircraft(parts[1], parts[2]);
                        if (aircraft != null) {
                            depot.enqueue(aircraft);
                        }
                        break;

                    default:
                        System.err.println("SaveLoadManager: unknown line prefix: " + parts[0]);
                        break;
                }
            }

            return true;

        } catch (IOException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.err.println("SaveLoadManager.load() failed: " + e.getMessage());
            return false;
        }
    }

  
    private static Aircraft reconstructAircraft(String type, String flightNumber) {
        switch (type) {
            case "CommercialJet":   return new model.CommercialJet(flightNumber);
            case "CargoFreighter":  return new model.CargoFreighter(flightNumber);
            case "PrivateCharter":  return new model.PrivateCharter(flightNumber);
            default:
                System.err.println("SaveLoadManager: unknown aircraft type: " + type);
                return null;
        }
    }
}