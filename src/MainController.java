import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import model.DepotManager;
import model.SupplyItem;

public class MainController {

    //  UI references 
    private ListView<String> flightListView;
    private Label fuelLabel;
    private Label mealsLabel;
    private Label cartsLabel;
    private Label budgetLabel;
    private TextArea logArea;
    private ComboBox<String> supplyDropdown;

    //  Backend 
    private DepotManager depot = new DepotManager();

    // BUILD THE FULL UI — called by App.java
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

        // Show real depot values immediately on startup
        refreshResourceDisplay();

        return root;
    }

    // ZONE 1 — The Holding Pattern (Queue)
    private VBox buildQueuePanel() {
        VBox panel = createPanel("Zone 1 — The Holding Pattern");
        panel.setPrefWidth(420);

        flightListView = new ListView<>();
        flightListView.setPrefHeight(200);

        // Dummy flights — will be replaced by Person A's timer in Week 2
        flightListView.getItems().addAll(
            "FL-101 | Boeing 747      | Fuel: 8000L | Meals: 300",
            "FL-202 | Cargo Freighter | Fuel: 5000L | Meals: 0",
            "FL-303 | Private Charter | Fuel: 1000L | Meals: 50"
        );

        Label queueCount = new Label("Flights waiting: " + flightListView.getItems().size());
        queueCount.setTextFill(Color.web("#555555"));
        queueCount.setFont(Font.font("Arial", 12));

        // Update count label whenever list changes
        flightListView.getItems().addListener(
            (javafx.collections.ListChangeListener<String>) change ->
                queueCount.setText("Flights waiting: " + flightListView.getItems().size())
        );

        Button clearBtn = new Button("Clear Next Flight");
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        styleButton(clearBtn, "#1565C0");

        // LAMBDA — event handling only
        clearBtn.setOnAction(e -> handleClearFlight());

        panel.getChildren().addAll(flightListView, queueCount, clearBtn);
        return panel;
    }

    // ZONE 2 — Terminal Depot (Resource State)
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

    // ZONE 3 — Supply Requisition (Supply Chain)
    private VBox buildSupplyPanel() {
        VBox panel = createPanel("Zone 3 — Supply Requisition");
        panel.setPrefWidth(300);

        Label instructions = new Label("Select a supply and click Purchase to restock.\nCost: $5,000 per order.");
        instructions.setWrapText(true);
        instructions.setTextFill(Color.web("#555555"));
        instructions.setFont(Font.font("Arial", 12));

        supplyDropdown = new ComboBox<>();
        supplyDropdown.getItems().addAll("Jet Fuel", "In-flight Meals", "Baggage Carts");
        supplyDropdown.setValue("Jet Fuel");
        supplyDropdown.setMaxWidth(Double.MAX_VALUE);

        Button purchaseBtn = new Button("Purchase Cargo");
        purchaseBtn.setMaxWidth(Double.MAX_VALUE);
        styleButton(purchaseBtn, "#2e7d32");

        // LAMBDA — delegates to handlePurchase()
        purchaseBtn.setOnAction(e -> handlePurchase(supplyDropdown.getValue()));

        panel.getChildren().addAll(instructions, supplyDropdown, purchaseBtn);
        return panel;
    }

    // ZONE 4 — Dispatch Radio (System Log)
    private VBox buildLogPanel() {
        VBox panel = createPanel("Zone 4 — Dispatch Radio");

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setPrefHeight(200);
        logArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12;");
        VBox.setVgrow(logArea, Priority.ALWAYS);

        panel.getChildren().add(logArea);

        log("INFO: Skyways Ground Operations Dashboard started.");
        log("INFO: Depot loaded — Fuel: " + depot.getSupply(SupplyItem.FUEL)
            + "L | Meals: " + depot.getSupply(SupplyItem.MEALS)
            + " | Budget: $" + String.format("%,d", depot.getBudget()));
        log("INFO: Awaiting incoming flights...");

        return panel;
    }

    // SAVE / LOAD ROW
    private HBox buildSaveLoadRow() {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_RIGHT);

        Button saveBtn = new Button("Save State");
        Button loadBtn = new Button("Load State");

        styleButton(saveBtn, "#6a1b9a");
        styleButton(loadBtn, "#6a1b9a");

        // LAMBDAS — delegate to handler methods
        saveBtn.setOnAction(e -> handleSave());
        loadBtn.setOnAction(e -> handleLoad());

        row.getChildren().addAll(saveBtn, loadBtn);
        return row;
    }

    // HANDLER METHODS — business logic lives here, not in lambdas

    private void handleClearFlight() {
        if (flightListView.getItems().isEmpty()) {
            log("WARNING: No flights in the holding pattern. Standby.");
            return;
        }

        String next = flightListView.getItems().get(0);

        // Basic fuel check — Week 2 will replace this with real Aircraft object check
        if (depot.getSupply(SupplyItem.FUEL) < 1000) {
            log("ERROR: Cannot clear [" + next + "] — Insufficient Jet Fuel!");
            return;
        }

        if (depot.getSupply(SupplyItem.MEALS) < 50) {
            log("ERROR: Cannot clear [" + next + "] — Insufficient In-flight Meals!");
            return;
        }

        // Remove from queue and consume resources
        flightListView.getItems().remove(0);
        depot.consumeResources(1000, 50, 8000);

        log("SUCCESS: Cleared flight — " + next);
        log("INFO: Resources consumed. Revenue +$8,000 added to budget.");

        refreshResourceDisplay();
        checkLowResources();
    }

    private void handlePurchase(String selectedSupply) {
        if (selectedSupply == null) {
            log("ERROR: No supply selected from dropdown.");
            return;
        }

        SupplyItem item = mapToSupplyItem(selectedSupply);

        if (item == null) {
            log("ERROR: Unknown supply item selected.");
            return;
        }

        boolean success = depot.purchase(item);

        if (success) {
            log("SUCCESS: Purchased " + selectedSupply
                + " — $5,000 deducted. +1000 units added.");
        } else {
            log("ERROR: Cannot purchase " + selectedSupply
                + " — Insufficient budget! Current: $"
                + String.format("%,d", depot.getBudget()));
        }

        refreshResourceDisplay();
    }

    private void handleSave() {
        // TODO Week 2: call SaveLoadManager.save(depot, queue)
        log("INFO: Save requested — SaveLoadManager not yet connected.");
    }

    private void handleLoad() {
        // TODO Week 2: call SaveLoadManager.load(), then refreshResourceDisplay()
        log("INFO: Load requested — SaveLoadManager not yet connected.");
        refreshResourceDisplay();
    }

    // PUBLIC METHOD — Person A calls this when timer generates a new flight
    public void addFlightToQueue(String flightDescription) {
        flightListView.getItems().add(flightDescription);
        log("WARNING: New Arrival — " + flightDescription);
    }

    // HELPER METHODS

    public void refreshResourceDisplay() {
        int fuel   = depot.getSupply(SupplyItem.FUEL);
        int meals  = depot.getSupply(SupplyItem.MEALS);
        int carts  = depot.getSupply(SupplyItem.BAGGAGE_CARTS);
        int budget = depot.getBudget();

        fuelLabel.setText ("Jet Fuel:        " + fuel  + " L");
        mealsLabel.setText("In-flight Meals: " + meals);
        cartsLabel.setText("Baggage Carts:   " + carts);
        budgetLabel.setText("Budget:          $" + String.format("%,d", budget));

        // Turn label red if critically low
        fuelLabel.setTextFill(fuel < 2000   ? Color.web("#c62828") : Color.web("#1a1a2e"));
        mealsLabel.setTextFill(meals < 100  ? Color.web("#c62828") : Color.web("#1a1a2e"));
    }

    public void log(String message) {
        java.time.LocalTime now = java.time.LocalTime.now();
        String timestamp = String.format("[%02d:%02d:%02d] ",
            now.getHour(), now.getMinute(), now.getSecond());
        logArea.appendText(timestamp + message + "\n");
        logArea.setScrollTop(Double.MAX_VALUE);
    }

    private void checkLowResources() {
        if (depot.getSupply(SupplyItem.FUEL) < 2000) {
            log("WARNING: Jet Fuel critically low! Consider restocking.");
        }
        if (depot.getSupply(SupplyItem.MEALS) < 100) {
            log("WARNING: In-flight Meals critically low! Consider restocking.");
        }
    }

    // Maps dropdown string to model.SupplyItem enum
    private SupplyItem mapToSupplyItem(String label) {
        switch (label) {
            case "Jet Fuel":        return SupplyItem.FUEL;
            case "In-flight Meals": return SupplyItem.MEALS;
            case "Baggage Carts":   return SupplyItem.BAGGAGE_CARTS;
            default:                return null;
        }
    }

    // STYLING HELPERS
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

        Separator sep = new Separator();
        panel.getChildren().addAll(header, sep);
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