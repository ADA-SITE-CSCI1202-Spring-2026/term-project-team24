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

        //update for automatic flight generation
        FlightGenerator generator = new FlightGenerator(controller);
        generator.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}