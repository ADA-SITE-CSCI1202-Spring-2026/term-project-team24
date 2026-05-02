package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        MainController controller = new MainController();
        Scene scene = new Scene(controller.buildUI(), 900, 650);
        stage.setTitle("Skyways — Ground Operations Dashboard");
        stage.setScene(scene);
        stage.show();
        // SimulationEngine starts automatically inside controller.buildUI()
        // FlightGenerator removed — engine handles flight generation internally
    }

    public static void main(String[] args) {
        launch(args);
    }
}