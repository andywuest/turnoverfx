package de.jos.javafx.turnover.dataprovider;

import java.math.BigDecimal;

// TODO record
public class TurnoverData {

    private String yearMonth;

    private Integer year;

    private Integer month;

    private BigDecimal turnover;

    private BigDecimal turnoverSum;

    private BigDecimal hours;

    private BigDecimal hoursSum;

    public TurnoverData() {
    }

    public TurnoverData(TurnoverData turnoverData) {
        super();
        this.yearMonth = turnoverData.yearMonth;
        this.year = turnoverData.year;
        this.month = turnoverData.month;
        this.turnover = turnoverData.turnover;
        this.turnoverSum = turnoverData.turnoverSum;
        this.hours = turnoverData.hours;
        this.hoursSum = turnoverData.hoursSum;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public void setHours(BigDecimal hours) {
        this.hours = hours;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public BigDecimal getTurnoverSum() {
        return turnoverSum;
    }

    public void setTurnoverSum(BigDecimal turnoverSum) {
        this.turnoverSum = turnoverSum;
    }

    public BigDecimal getHoursSum() {
        return hoursSum;
    }

    public void setHoursSum(BigDecimal hoursSum) {
        this.hoursSum = hoursSum;
    }

    @Override
    public String toString() {
        return "TurnoverData{" + "yearMonth=" + yearMonth + ", year=" + year
                + ", month=" + month + ", turnover=" + turnover
                + ", turnoverSum=" + turnoverSum + ", hours=" + hours
                + ", hoursSum=" + hoursSum + '}';
    }

}
