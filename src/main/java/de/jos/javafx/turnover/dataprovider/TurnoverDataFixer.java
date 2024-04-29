package de.jos.javafx.turnover.dataprovider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Fixes missing entries in the list of turnovers. Otherwise the charts will
 * most likely be broken. Thus we replace the gaps with empty entries.
 */
public class TurnoverDataFixer {

    private static final Logger LOGGER = Logger.getLogger(TurnoverDataFixer.class.getName());

    public TurnoverDataFixer() {
    }

    /**
     * Fills the gaps with empty entries.
     *
     * @param turnoverDataList The list of turnoverData.
     */
    public void fillMissingGaps(List<TurnoverData> turnoverDataList) {

        final List<String> yearMonthList = new ArrayList<>();

        int firstYear = 3000;
        int lastYear = 0;

        // determine the first and last year - also fill the yearMonthList
        for (TurnoverData currentTurnoverData : turnoverDataList) {
            yearMonthList.add(currentTurnoverData.getYearMonth());
            if (firstYear > currentTurnoverData.getYear().intValue()) {
                firstYear = currentTurnoverData.getYear().intValue();
            }
            if (lastYear < currentTurnoverData.getYear().intValue()) {
                lastYear = currentTurnoverData.getYear().intValue();
            }
        }

        LOGGER.log(Level.INFO, "List of yearMonths : {0}", yearMonthList);

        String lastYearMonthListKey = null;
        boolean lastFound = false;
        // run through the months and find the last year / month for which we
        // have data - exit criteria for next step
        for (int currentLastYear = lastYear; currentLastYear >= firstYear; currentLastYear--) {
            for (int currentLastMonth = 12; currentLastMonth >= 1; currentLastMonth--) {
                String yearMonthListKey = String.valueOf(currentLastYear)
                        + String.format("%02d", currentLastMonth);
                LOGGER.log(Level.INFO, "Currently handled year month {0}", yearMonthListKey);
                if (!lastFound && yearMonthList.contains(yearMonthListKey)) {
                    lastYearMonthListKey = yearMonthListKey;
                    lastFound = true;
                    LOGGER.log(Level.INFO, "Last to handle found {0}", lastYearMonthListKey);
                }
            }
        }

        if (lastYearMonthListKey == null) {
            LOGGER.log(Level.WARNING, "lastYearMonthListKey could not be determined !");
            return;
        }

        LOGGER.log(Level.INFO, "last to handle {0}", lastYearMonthListKey);

        // check for gaps and fill them.
        for (int currentYear = firstYear; currentYear <= lastYear; currentYear++) {
            TurnoverData lastTurnoverData = null;
            for (int currentMonth = 1; currentMonth <= 12; currentMonth++) {
                final String yearMonthListKey = String.valueOf(currentYear)
                        + String.format("%02d", currentMonth);
                // run until we found the last element
                if (lastYearMonthListKey.equals(yearMonthListKey)) {
                    break;
                }

                if (yearMonthList.contains(yearMonthListKey)) {
                    lastTurnoverData = getTurnoverDataForYearMonth(
                            turnoverDataList, yearMonthListKey);
                } else {
                    TurnoverData tmpTurnoverData = null;
                    if (lastTurnoverData == null) {
                        // create new entry with zero values
                        tmpTurnoverData = new TurnoverData();
                        tmpTurnoverData.setYearMonth(yearMonthListKey);
                        tmpTurnoverData.setYear(Integer.valueOf(currentYear));
                        tmpTurnoverData.setMonth(Integer.valueOf(currentMonth));
                        tmpTurnoverData.setHours(BigDecimal.ZERO);
                        tmpTurnoverData.setTurnover(BigDecimal.ZERO);
                        tmpTurnoverData.setHoursSum(BigDecimal.ZERO);
                        tmpTurnoverData.setTurnoverSum(BigDecimal.ZERO);
                        turnoverDataList.add(tmpTurnoverData);
                        LOGGER.log(Level.INFO, "added turnoverData {0}",
                                tmpTurnoverData);
                    } else {
                        // copy last entry
                        tmpTurnoverData = new TurnoverData(lastTurnoverData);
                        // update the year month / values
                        tmpTurnoverData.setYearMonth(yearMonthListKey);
                        tmpTurnoverData.setYear(Integer.valueOf(currentYear));
                        tmpTurnoverData.setMonth(Integer.valueOf(currentMonth));
                        // and update the number of hours / turnover with zero
                        tmpTurnoverData.setHours(BigDecimal.ZERO);
                        tmpTurnoverData.setTurnover(BigDecimal.ZERO);
                        turnoverDataList.add(tmpTurnoverData);
                        LOGGER.log(Level.INFO, "added turnoverData {0}",
                                tmpTurnoverData);
                    }

                    // the last one is the just created one
                    lastTurnoverData = tmpTurnoverData;

                    // sort the list after the insert
                    Collections.sort(turnoverDataList,
                            TurnoverDataSorter.INSTANCE);
                }
            }
        }
    }

    /**
     * Provides the turnover data element from the list for the given
     * yearMonthListKey.
     *
     * @param turnoverDataList The list of TurnoverData.
     * @param yearMonthListKey The key to search for.
     * @return The found TurnoverData element.
     */
    private TurnoverData getTurnoverDataForYearMonth(
            List<TurnoverData> turnoverDataList, String yearMonthListKey) {
        for (TurnoverData turnoverData : turnoverDataList) {
            if (turnoverData.getYearMonth().equals(yearMonthListKey)) {
                return turnoverData;
            }
        }
        return null;
    }

}
