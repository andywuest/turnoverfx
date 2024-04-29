package de.jos.javafx.turnover.charting;

import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Named;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.logging.Logger;

@Named
@Alternative
public class LineChartProducer extends AbstractChartProducer {

    private final static Logger LOGGER = Logger.getLogger(LineChartProducer.class
            .getName());

    public LineChartProducer() {
        LOGGER.info("Using LineChartProducer");
    }

    @Override
    protected XYChart<String, Number> createSpecificChart(CategoryAxis xAxis,
                                                          NumberAxis yAxis) {
        return new LineChart<>(xAxis, yAxis);
    }

}
