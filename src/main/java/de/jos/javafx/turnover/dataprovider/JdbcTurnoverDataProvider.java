package de.jos.javafx.turnover.dataprovider;

import jakarta.inject.Named;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author andy
 */
@Named
public class JdbcTurnoverDataProvider implements TurnoverDataProvider {

    private static final Logger LOGGER = Logger.getLogger(JdbcTurnoverDataProvider.class.getName());

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/kraft";

    private static final String QUERY = "select date_format(date, '%Y-%m') as workmonth, sum(amount) as hours, sum(amount * price) as turnover from document d left join docposition dp on d.docId = dp.docId group by workmonth";

    private static final String QUERY_BY_YEAR = "select date_format(date, '%Y') as workyear, sum(amount), sum(amount * price) from document d left join docposition dp on d.docId = dp.docId group by workyear";

    final Properties jdbcProperties = new Properties();

    public JdbcTurnoverDataProvider() {
        jdbcProperties.put("user", "kraft");
        jdbcProperties.put("password", "kraft");
        jdbcProperties.put("useUnicode", "true");
        jdbcProperties.put("characterEncoding", "utf8");
    }

    @Override
    public List<TurnoverData> getTurnoverData() {

        // Create a ResultSetHandler implementation to convert the
        // first row into an Object[].
        final ResultSetHandler<List<TurnoverData>> resultSetHandler = new ResultSetHandler<List<TurnoverData>>() {
            @Override
            public List<TurnoverData> handle(ResultSet rs) throws SQLException {
                if (!rs.next()) {
                    return Collections.emptyList();
                }

                LOGGER.log(Level.INFO, "Starting to read values from database.");

                final List<TurnoverData> resultList = new ArrayList<>();
                do {
                    final TurnoverData result = new TurnoverData();
                    String yearMonth = rs.getString(1);
                    String[] tokens = yearMonth.split("-");
                    // set the new year month, without the dash !
                    result.setYearMonth(tokens[0] + tokens[1]);
                    result.setYear(Integer.valueOf(tokens[0]));
                    result.setMonth(Integer.valueOf(tokens[1]));
                    result.setHours(rs.getBigDecimal(2));
                    result.setTurnover(rs.getBigDecimal(3));
                    LOGGER.log(Level.INFO, "  => read value {0}",
                            result.toString());
                    resultList.add(result);
                } while (rs.next());

                return resultList;
            }
        };

        List<TurnoverData> result = null;

        try (Connection connection = DriverManager.getConnection(JDBC_URL,
                jdbcProperties)) {
            final QueryRunner run = new QueryRunner();
            result = run.query(connection, QUERY, resultSetHandler);
            // sort result
            Collections.sort(result, TurnoverDataSorter.INSTANCE);
        } catch (SQLException e) {
            System.err.println("Exception occured running query : "
                    + e.getMessage());
        }

        Integer currentYear = null;
        BigDecimal sumHours = BigDecimal.ZERO;
        BigDecimal sumTurnovers = BigDecimal.ZERO;

        // update the sums
        if (result != null) {
            for (TurnoverData turnoverData : result) {
                // new year, reset the sum
                if (!turnoverData.getYear().equals(currentYear)) {
                    sumHours = BigDecimal.ZERO;
                    sumTurnovers = BigDecimal.ZERO;
                    currentYear = turnoverData.getYear();
                }
                // update the sum and update the turnover data.
                sumTurnovers = sumTurnovers.add(turnoverData.getTurnover());
                sumHours = sumHours.add(turnoverData.getHours());
                turnoverData.setTurnoverSum(sumTurnovers);
                turnoverData.setHoursSum(sumHours);
            }
        }

        return result;
    }

}
