import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.Random;
import model.CommercialJet;
import model.CargoFreighter;
import model.PrivateCharter;
import model.Aircraft;

public class FlightGenerator {
    private final MainController controller;
    private final Random random = new Random();
    private int flightCounter = 100;
    private Timeline timer;

    public FlightGenerator(MainController controller) {
        this.controller = controller;
    }

    public void start() {
        timer = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> generateFlight())
        );
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }




    private void generateFlight() {
        flightCounter++;
        String number = "FL-" + flightCounter;

        Aircraft aircraft = switch (random.nextInt(3)) {
            case 0 -> new CommercialJet(number);
            case 1 -> new CargoFreighter(number);
            default -> new PrivateCharter(number);
        };

        controller.addFlightToQueue(aircraft);
    }



    public void stop() {
        if (timer != null) timer.stop();
    }
}