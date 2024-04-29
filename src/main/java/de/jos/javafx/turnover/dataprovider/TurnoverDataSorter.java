package de.jos.javafx.turnover.dataprovider;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author awuest
 */
public class TurnoverDataSorter implements Comparator<TurnoverData>, Serializable {

    private static final long serialVersionUID = 1L;

    public static final TurnoverDataSorter INSTANCE = new TurnoverDataSorter();

    @Override
    public int compare(TurnoverData turnoverData1, TurnoverData turnoverData2) {
        return turnoverData1.getYearMonth().compareTo(
                turnoverData2.getYearMonth());
    }

}
