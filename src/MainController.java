import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

public class MainController {

    private ListView<String> flightListView;
    private Label fuelLabel;
    private Label mealsLabel;
    private Label cartsLabel;
    private Label budgetLabel;
    private TextArea logArea;
    private ComboBox<String> supplyDropdown;

    private DepotManager depot = new DepotManager();

 
    public VBox buildUI() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(16));
        root.setStyle("-fx-background-color: #f5f5f5;");

        Label title = new Label("Skyways International — Ground Operations Dashboard");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#1a1a2e"));

        // Top row: queue (left) + resources (right)
        HBox topRow = new HBox(12);
        topRow.getChildren().addAll(buildQueuePanel(), buildResourcePanel());
        HBox.setHgrow(topRow.getChildren().get(0), Priority.ALWAYS);

        // Bottom row: supply chain (left) + log (right)
        HBox bottomRow = new HBox(12);
        bottomRow.getChildren().addAll(buildSupplyPanel(), buildLogPanel());
        HBox.setHgrow(bottomRow.getChildren().get(1), Priority.ALWAYS);

        // Save / Load buttons at the very bottom
        HBox saveLoadRow = buildSaveLoadRow();

        root.getChildren().addAll(title, topRow, bottomRow, saveLoadRow);
        return root;
    }

    // -------------------------------------------------------
    // ZONE 1 — The Holding Pattern (Queue)
    // -------------------------------------------------------
    private VBox buildQueuePanel() {
        VBox panel = createPanel("Zone 1 — The Holding Pattern");
        panel.setPrefWidth(420);

        flightListView = new ListView<>();
        flightListView.setPrefHeight(200);

        // Dummy data so the layout looks right before backend is connected
        flightListView.getItems().addAll(
            "FL-101 | Boeing 747 | Fuel: 8000L | Meals: 300",
            "FL-202 | Cargo Freighter | Fuel: 5000L",
            "FL-303 | Private Charter | Meals: 50"
        );

        Button clearBtn = new Button("Clear Next Flight");
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        styleButton(clearBtn, "#1565C0");

        // LAMBDA — event handling only, logic stays in handleClearFlight()
        clearBtn.setOnAction(e -> handleClearFlight());

        panel.getChildren().addAll(flightListView, clearBtn);
        return panel;
    }

    // -------------------------------------------------------
    // ZONE 2 — Terminal Depot (Resource State)
    // -------------------------------------------------------
    private VBox buildResourcePanel() {
        VBox panel = createPanel("Zone 2 — Terminal Depot");
        panel.setPrefWidth(360);

        fuelLabel    = new Label("Jet Fuel:       5000 L");
        mealsLabel   = new Label("In-flight Meals: 400");
        cartsLabel   = new Label("Baggage Carts:   15");
        budgetLabel  = new Label("Budget:          $50,000");

        styleResourceLabel(fuelLabel);
        styleResourceLabel(mealsLabel);
        styleResourceLabel(cartsLabel);
        styleResourceLabel(budgetLabel);

        budgetLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        budgetLabel.setTextFill(Color.web("#2e7d32"));

        panel.getChildren().addAll(
            fuelLabel, mealsLabel, cartsLabel,
            new Separator(), budgetLabel
        );
        return panel;
    }

    // -------------------------------------------------------
    // ZONE 3 — Supply Requisition (Supply Chain)
    // -------------------------------------------------------
    private VBox buildSupplyPanel() {
        VBox panel = createPanel("Zone 3 — Supply Requisition");
        panel.setPrefWidth(300);

        Label instructions = new Label("Select a supply and click Purchase to restock.");
        instructions.setWrapText(true);
        instructions.setTextFill(Color.web("#555555"));

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

    // -------------------------------------------------------
    // ZONE 4 — Dispatch Radio (System Log)
    // -------------------------------------------------------
    private VBox buildLogPanel() {
        VBox panel = createPanel("Zone 4 — Dispatch Radio");

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setPrefHeight(200);
        logArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12;");
        VBox.setVgrow(logArea, Priority.ALWAYS);

        // Seed the log with startup messages
        log("INFO: Skyways Ground Operations Dashboard started.");
        log("INFO: Awaiting incoming flights...");

        panel.getChildren().add(logArea);
        return panel;
    }

    // -------------------------------------------------------
    // SAVE / LOAD ROW
    // -------------------------------------------------------
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

    // -------------------------------------------------------
    // HANDLER METHODS — business logic lives here, not in lambdas
    // -------------------------------------------------------
    private void handleClearFlight() {
        // TODO Week 2: replace with real queue.poll() + processing logic
        if (flightListView.getItems().isEmpty()) {
            log("WARNING: No flights in the holding pattern.");
            return;
        }
        String next = flightListView.getItems().remove(0);
        log("SUCCESS: Cleared flight — " + next);
        refreshResourceDisplay();
    }

    private void handlePurchase(String selectedSupply) {
        // TODO Week 2: map selectedSupply string to SupplyItem enum + call depot.purchase()
        if (selectedSupply == null) {
            log("ERROR: No supply selected.");
            return;
        }
        log("Purchased: " + selectedSupply + " — $5,000 deducted from budget.");
        refreshResourceDisplay();
    }

    private void handleSave() {
        // TODO Week 2: call SaveLoadManager.save(depot, queue)
        log("INFO: State saved to tower_log.txt");
    }

    private void handleLoad() {
        // TODO Week 2: call SaveLoadManager.load(), then refreshResourceDisplay() + rebuild queue list
        log("INFO: State loaded from tower_log.txt");
        refreshResourceDisplay();
    }

    // -------------------------------------------------------
    // HELPER METHODS — called after every action
    // -------------------------------------------------------

    // Call this every time resources or budget change
    public void refreshResourceDisplay() {
        fuelLabel.setText  ("Jet Fuel:        " + depot.getSupply(SupplyItem.FUEL)          + " L");
        mealsLabel.setText ("In-flight Meals: " + depot.getSupply(SupplyItem.MEALS));
        cartsLabel.setText ("Baggage Carts:   " + depot.getSupply(SupplyItem.BAGGAGE_CARTS));
        budgetLabel.setText("Budget:          $" + String.format("%,d", depot.getBudget()));
    }

    // Call this whenever something happens that should appear in the log
    public void log(String message) {
        java.time.LocalTime now = java.time.LocalTime.now();
        String timestamp = String.format("[%02d:%02d:%02d] ",
            now.getHour(), now.getMinute(), now.getSecond());
        logArea.appendText(timestamp + message + "\n");
        // Auto-scroll to the bottom
        logArea.setScrollTop(Double.MAX_VALUE);
    }

    // -------------------------------------------------------
    // STYLING HELPERS — keep visual code out of logic
    // -------------------------------------------------------
    private VBox createPanel(String title) {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(12));
        panel.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #cccccc;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        Label header = new Label(title);
        header.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        header.setTextFill(Color.web("#1a1a2e"));
        panel.getChildren().add(header);
        return panel;
    }

    private void styleButton(Button btn, String color) {
        btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 16;" +
            "-fx-background-radius: 6;"
        );
    }

    private void styleResourceLabel(Label lbl) {
        lbl.setFont(Font.font("Courier New", 13));
        lbl.setTextFill(Color.web("#1a1a2e"));
    }
}