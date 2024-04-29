package de.jos.javafx.turnover.dataprovider;

import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Named;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Creates dummy turnover data (turnover / hours).
 *
 * @author awuest
 */
@Named
@Alternative
public class SampleTurnoverDataProvider implements TurnoverDataProvider {

    private static final BigDecimal PRICE_PER_HOUR = new BigDecimal("70.0");

    @Override
    public List<TurnoverData> getTurnoverData() {

        final List<TurnoverData> turnoverList = new ArrayList<>();

        final Random random = new Random(System.currentTimeMillis());

        for (int y = 2008; y < 2015; y++) {
            BigDecimal turnoverSum = BigDecimal.ZERO;
            BigDecimal hoursSum = BigDecimal.ZERO;

            for (int m = 1; m <= 12; m++) {
                // create some gaps in the data
                if (y == 2008) {
                    if (m < 10) {
                        continue;
                    }
                }
                if (y == 2009) {
                    if (m < 3) {
                        continue;
                    }
                }
                if (y == 2014 && m > 8) {
                    continue;
                }
                if (y == 2010 && m > 6 && m < 9) {
                    continue;
                }

                final TurnoverData turnoverData = new TurnoverData();
                final BigDecimal hours = new BigDecimal(random.nextInt(160));
                hoursSum = hoursSum.add(hours);
                final BigDecimal turnover = hours.multiply(PRICE_PER_HOUR);
                turnoverSum = turnoverSum.add(turnover);
                turnoverData.setYearMonth(String.valueOf(y)
                        + String.format("%02d", m));
                turnoverData.setMonth(Integer.valueOf(m));
                turnoverData.setYear(Integer.valueOf(y));
                turnoverData.setHours(hours);
                turnoverData.setTurnover(turnover);
                turnoverData.setHoursSum(hoursSum);
                turnoverData.setTurnoverSum(turnoverSum);
                turnoverList.add(turnoverData);
            }
        }

        return turnoverList;
    }

}
