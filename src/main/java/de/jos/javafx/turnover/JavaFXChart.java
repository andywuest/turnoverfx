package de.jos.javafx.turnover;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;

import java.util.logging.Logger;

public class JavaFXChart extends Application {

    private final static Logger LOGGER = Logger.getLogger(JavaFXChart.class.getName());

    private Weld weld;

    @Override
    public void init() {
        weld = new Weld();
    }

    @Override
    public void start(Stage stage) {
        weld.initialize().instance().select(TurnoverChartMain.class).get()
                .run(stage);
    }

    @Override
    public void stop() {
        weld.shutdown();
    }

    public static void main(String[] args) {
        LOGGER.info("JavaFX Version : " + System.getProperty("javafx.version"));
        launch(args);
    }

}
