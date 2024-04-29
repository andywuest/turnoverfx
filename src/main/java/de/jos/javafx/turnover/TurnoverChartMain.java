package de.jos.javafx.turnover;

import de.jos.javafx.turnover.charting.AbstractChartProducer;
import de.jos.javafx.turnover.charting.AggregationType;
import de.jos.javafx.turnover.dataprovider.TurnoverData;
import de.jos.javafx.turnover.dataprovider.TurnoverDataFixer;
import de.jos.javafx.turnover.dataprovider.TurnoverDataProvider;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
public class TurnoverChartMain {

    private final static Logger LOGGER = Logger.getLogger(TurnoverChartMain.class.getName());

    @Inject
    AbstractChartProducer chartProducer;

    @Inject
    TurnoverDataProvider turnoverDataProvider;

    public TurnoverChartMain() {
    }

    public void run(Stage stage) {
        stage.setTitle("Umsatzentwicklung");

        // vertical box
        final VBox vbox = new VBox();

        // main scene
        final Scene scene = new Scene(vbox, 950, 700);
        scene.getStylesheets().add("style.css");

        LOGGER.info(String.format("Using %s", turnoverDataProvider.getClass()
                .getSimpleName()));

        final List<TurnoverData> turnoverData = turnoverDataProvider
                .getTurnoverData();

        new TurnoverDataFixer().fillMissingGaps(turnoverData);

        LOGGER.log(Level.INFO, "Sorted turnover data is : {0}", turnoverData);

        vbox.getChildren().addAll(
                chartProducer.createLineChart("Monate", "Umsatz in Euro",
                        "Umsatzentwicklung im Jahr", turnoverData,
                        AggregationType.TURNOVER),
                chartProducer.createLineChart("Monate", "Stunden",
                        "Entwicklung geleistete Stunden im Jahr", turnoverData,
                        AggregationType.HOURS));

        stage.setScene(scene);
        stage.show();
    }

}
