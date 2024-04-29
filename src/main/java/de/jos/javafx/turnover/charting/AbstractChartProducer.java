package de.jos.javafx.turnover.charting;

import de.jos.javafx.turnover.dataprovider.TurnoverData;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractChartProducer {

    private static final Logger LOGGER = Logger.getLogger(AbstractChartProducer.class
            .getName());

    protected abstract XYChart<String, Number> createSpecificChart(
            CategoryAxis xAxis, NumberAxis yAxis);

    public XYChart<String, Number> createLineChart(String labelX,
                                                   String labelY, String title, List<TurnoverData> turnovers,
                                                   AggregationType aggregationType) {

        LOGGER.log(Level.INFO, "Aggregation type is: {0}", aggregationType);

        // defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(labelX);
        yAxis.setLabel(labelY);

        final XYChart<String, Number> xyChart = createSpecificChart(xAxis,
                yAxis);

        xyChart.setTitle(title);

        Integer currentYear = null;
        XYChart.Series<String, Number> series = null;

        if (turnovers != null) {
            for (TurnoverData turnoverData : turnovers) {
                final Integer year = turnoverData.getYear();
                // we have a new year
                if (!year.equals(currentYear)) {
                    // create new chart series and set label
                    series = new XYChart.Series<>();
                    series.setName(year.toString());
                    xyChart.getData().add(series);
                    currentYear = year;
                }

                BigDecimal value = null;
                if (aggregationType == AggregationType.HOURS) {
                    value = turnoverData.getHoursSum();
                } else if (aggregationType == AggregationType.TURNOVER) {
                    value = turnoverData.getTurnoverSum();
                }

                if (series != null) {
                    final String translatedMonth = translateMonth(turnoverData
                            .getMonth());
                    LOGGER.log(Level.INFO, "Added series : {0} {1} - {2}",
                            new Object[]{String.valueOf(currentYear),
                                    translatedMonth, value});
                    series.getData().add(
                            new XYChart.Data<>(translatedMonth,
                                    value));
                }
            }
        }

        return xyChart;
    }

    private String translateMonth(Integer month) {
        final Calendar cal = new GregorianCalendar();
        cal.set(Calendar.MONTH, month.intValue() - 1);
        return new SimpleDateFormat("MMM").format(cal.getTime());
    }

}
