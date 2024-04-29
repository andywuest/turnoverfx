package de.jos.javafx.turnover.charting;

import jakarta.inject.Named;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.logging.Logger;

@Named
public class BarChartProducer extends AbstractChartProducer {

    private final static Logger LOGGER = Logger.getLogger(BarChartProducer.class.getName());

    public BarChartProducer() {
        LOGGER.info("Using BarChartProducer");
    }

    @Override
    protected XYChart<String, Number> createSpecificChart(CategoryAxis xAxis,
                                                          NumberAxis yAxis) {
        return new BarChart<>(xAxis, yAxis);
    }

}
