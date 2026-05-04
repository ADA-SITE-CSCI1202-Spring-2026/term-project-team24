package app;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import model.Aircraft;
import model.DepotManager;
import model.SupplyItem;
import service.SimulationEngine;
import service.SaveLoadManager;

public class MainController {

    private ListView<String> flightListView;
    private Label fuelLabel;
    private Label mealsLabel;
    private Label cartsLabel;
    private Label budgetLabel;
    private Label queueCount;
    private TextArea logArea;
    private ComboBox<SupplyItem> supplyDropdown;

    private final DepotManager depot = new DepotManager();
    private final SimulationEngine engine;

    public MainController() {
        engine = new SimulationEngine(depot);

        engine.setOnQueueUpdated(this::refreshQueueDisplay);
        engine.setOnStateUpdated(this::refreshResourceDisplay);
        engine.setOnLog(this::log);
    }

    public VBox buildUI() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(16));
        root.setStyle("-fx-background-color: #f0f4f8;");

        Label title = new Label("Skyways International — Ground Operations Dashboard");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#1a1a2e"));

        HBox topRow = new HBox(12);
        topRow.getChildren().addAll(buildQueuePanel(), buildResourcePanel());
        HBox.setHgrow(topRow.getChildren().get(0), Priority.ALWAYS);

        HBox bottomRow = new HBox(12);
        bottomRow.getChildren().addAll(buildSupplyPanel(), buildLogPanel());
        HBox.setHgrow(bottomRow.getChildren().get(1), Priority.ALWAYS);

        HBox saveLoadRow = buildSaveLoadRow();

        root.getChildren().addAll(title, topRow, bottomRow, saveLoadRow);

        refreshResourceDisplay();

        engine.start();

        return root;
    }

    private VBox buildQueuePanel() {
        VBox panel = createPanel("Zone 1 — The Holding Pattern");
        panel.setPrefWidth(420);

        flightListView = new ListView<>();
        flightListView.setPrefHeight(200);

        queueCount = new Label("Flights waiting: 0");
        queueCount.setTextFill(Color.web("#555555"));
        queueCount.setFont(Font.font("Arial", 12));

        Button clearBtn = new Button("Clear Next Flight");
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        styleButton(clearBtn, "#1565C0");

        clearBtn.setOnAction(e -> handleClearFlight());

        panel.getChildren().addAll(flightListView, queueCount, clearBtn);
        return panel;
    }

    private VBox buildResourcePanel() {
        VBox panel = createPanel("Zone 2 — Terminal Depot");
        panel.setPrefWidth(360);

        fuelLabel   = new Label();
        mealsLabel  = new Label();
        cartsLabel  = new Label();
        budgetLabel = new Label();

        styleResourceLabel(fuelLabel);
        styleResourceLabel(mealsLabel);
        styleResourceLabel(cartsLabel);

        budgetLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 14));
        budgetLabel.setTextFill(Color.web("#2e7d32"));

        panel.getChildren().addAll(
            fuelLabel, mealsLabel, cartsLabel,
            new Separator(),
            budgetLabel
        );
        return panel;
    }

    private VBox buildSupplyPanel() {
        VBox panel = createPanel("Zone 3 — Supply Requisition");
        panel.setPrefWidth(300);

        Label instructions = new Label("Select a supply and click Purchase to restock.");
        instructions.setWrapText(true);
        instructions.setTextFill(Color.web("#555555"));
        instructions.setFont(Font.font("Arial", 12));

        supplyDropdown = new ComboBox<>();
        supplyDropdown.getItems().addAll(SupplyItem.values());
        supplyDropdown.setValue(SupplyItem.FUEL);
        supplyDropdown.setMaxWidth(Double.MAX_VALUE);

        supplyDropdown.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(SupplyItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getDisplayName());
            }
        });
        supplyDropdown.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(SupplyItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getDisplayName());
            }
        });

        Button purchaseBtn = new Button("Purchase Cargo");
        purchaseBtn.setMaxWidth(Double.MAX_VALUE);
        styleButton(purchaseBtn, "#2e7d32");

        purchaseBtn.setOnAction(e -> handlePurchase(supplyDropdown.getValue()));

        panel.getChildren().addAll(instructions, supplyDropdown, purchaseBtn);
        return panel;
    }

    private VBox buildLogPanel() {
        VBox panel = createPanel("Zone 4 — Dispatch Radio");

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setPrefHeight(200);
        logArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12;");
        VBox.setVgrow(logArea, Priority.ALWAYS);

        panel.getChildren().add(logArea);
        return panel;
    }

    private HBox buildSaveLoadRow() {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_RIGHT);

        Button saveBtn = new Button("💾 Save State");
        Button loadBtn = new Button("📂 Load State");

        styleButton(saveBtn, "#6a1b9a");
        styleButton(loadBtn, "#6a1b9a");

        saveBtn.setOnAction(e -> handleSave());
        loadBtn.setOnAction(e -> handleLoad());

        row.getChildren().addAll(saveBtn, loadBtn);
        return row;
    }

    private void handleClearFlight() {
        engine.processNextAircraft();
    }

    private void handlePurchase(SupplyItem item) {
        if (item == null) {
            log("ERROR: No supply selected from dropdown.");
            return;
        }
        engine.purchaseSupply(item);
        checkLowResources();
    }

    private void handleSave() {
        engine.stop();
        boolean success = SaveLoadManager.save(depot);
        if (success) {
            log("INFO: State saved to airport_state.csv");
        } else {
            log("ERROR: Save failed — check file permissions.");
        }
        engine.start();
    }

    private void handleLoad() {
        engine.stop();
        boolean success = SaveLoadManager.load(depot);
        if (success) {
            refreshQueueDisplay();
            refreshResourceDisplay();
            log("INFO: State loaded from airport_state.csv — simulation resumed.");
        } else {
            log("ERROR: Load failed — file may not exist yet. Save first.");
        }
        engine.start();
    }

    public void refreshQueueDisplay() {
        Platform.runLater(() -> {
            flightListView.getItems().clear();
            for (Aircraft a : engine.getQueueSnapshot()) {
                flightListView.getItems().add(formatAircraft(a));
            }
            queueCount.setText("Flights waiting: " + flightListView.getItems().size());
        });
    }

    public void refreshResourceDisplay() {
        Platform.runLater(() -> {
            int fuel  = depot.getSupplyAmount(SupplyItem.FUEL);
            int meals = depot.getSupplyAmount(SupplyItem.MEALS);
            int carts = depot.getSupplyAmount(SupplyItem.BAGGAGE_CARTS);
            double budget = depot.getBudget();

            fuelLabel.setText ("Jet Fuel:        " + fuel  + " L");
            mealsLabel.setText("In-flight Meals: " + meals + " units");
            cartsLabel.setText("Baggage Carts:   " + carts + " units");
            budgetLabel.setText("Budget:          $" + String.format("%,.0f", budget));

            fuelLabel.setTextFill(fuel < 2000  ? Color.web("#c62828") : Color.web("#1a1a2e"));
            mealsLabel.setTextFill(meals < 100 ? Color.web("#c62828") : Color.web("#1a1a2e"));
            cartsLabel.setTextFill(carts < 5   ? Color.web("#c62828") : Color.web("#1a1a2e"));
            budgetLabel.setTextFill(budget < 5000 ? Color.web("#c62828") : Color.web("#2e7d32"));
        });
    }

    public void log(String message) {
        Platform.runLater(() -> {
            java.time.LocalTime now = java.time.LocalTime.now();
            String timestamp = String.format("[%02d:%02d:%02d] ",
                now.getHour(), now.getMinute(), now.getSecond());
            logArea.appendText(timestamp + message + "\n");
            logArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    private String formatAircraft(Aircraft a) {
        StringBuilder sb = new StringBuilder();
        sb.append(a.toString());
        a.getRequiredSupplies().forEach((item, qty) ->
            sb.append(" | ").append(item.getDisplayName()).append(": ").append(qty).append(item.getUnit())
        );
        return sb.toString();
    }

    private void checkLowResources() {
        if (depot.getSupplyAmount(SupplyItem.FUEL) < 2000) {
            log("WARNING: Jet Fuel critically low! Consider restocking.");
        }
        if (depot.getSupplyAmount(SupplyItem.MEALS) < 100) {
            log("WARNING: In-flight Meals critically low! Consider restocking.");
        }
        if (depot.getSupplyAmount(SupplyItem.BAGGAGE_CARTS) < 5) {
            log("WARNING: Baggage Carts critically low! Consider restocking.");
        }
    }

    private VBox createPanel(String title) {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(12));
        panel.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #d0d7de;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );
        Label header = new Label(title);
        header.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        header.setTextFill(Color.web("#1a1a2e"));
        panel.getChildren().addAll(header, new Separator());
        return panel;
    }

    private void styleButton(Button btn, String color) {
        btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 16;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;"
        );
    }

    private void styleResourceLabel(Label lbl) {
        lbl.setFont(Font.font("Courier New", 13));
        lbl.setTextFill(Color.web("#1a1a2e"));
    }
}
