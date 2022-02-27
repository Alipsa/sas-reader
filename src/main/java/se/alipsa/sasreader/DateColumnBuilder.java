package se.alipsa.sasreader;

import com.epam.parso.Column;
import org.renjin.sexp.AtomicVector;
import org.renjin.sexp.StringArrayVector;
import org.renjin.sexp.StringVector;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class DateColumnBuilder implements ColumnBuilder {

  private static final List<String> formats = Arrays.asList(
      "B8601DA", "E8601DA", "DATE", "DAY", "DDMMYY", "DDMMYYB", "DDMMYYC", "DDMMYYD", "DDMMYYN", "DDMMYYP",
      "DDMMYYS", "WEEKDATE", "WEEKDATX", "WEEKDAY", "DOWNAME", "WORDDATE", "WORDDATX", "YYMM", "YYMMC", "YYMMD",
      "YYMMN", "YYMMP", "YYMMS", "YYMMDD", "YYMMDDB", "YYMMDDC", "YYMMDDD", "YYMMDDN", "YYMMDDP", "YYMMDDS", "YYMON",
      "YEAR", "JULDAY", "JULIAN", "MMDDYY", "MMDDYYC", "MMDDYYD", "MMDDYYN", "MMDDYYP", "MMDDYYS", "MMYY", "MMYYC",
      "MMYYD", "MMYYN", "MMYYP", "MMYYS", "MONNAME", "MONTH", "MONYY", "E8601DN", "E8601DT", "E8601DX", "E8601DZ",
      "E8601LX", "B8601DN", "B8601DT", "B8601DX", "B8601DZ", "B8601LX", "DATEAMPM", "DATETIME", "DTDATE", "DTMONYY",
      "DTWKDATX", "DTYEAR", "TOD", "MDYAMPM"
  );

  public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_DATE;
  public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private final StringArrayVector.Builder vector = new StringVector.Builder();

  public static boolean acceptsType(Column column) {
    return "Date".equals(column.getType().getSimpleName()) || formats.contains(column.getFormat().getName());
  }

  @Override
  public void addValue(Object[] row, int columnIndex) {
    Object val = row[columnIndex];
    if (val == null) {
      vector.addNA();
    } else {
      if (val instanceof LocalDate) {
        LocalDate ld = (LocalDate) val;
        vector.add(DATE_FORMAT.format(ld));
      } else {
        LocalDateTime ldt = (LocalDateTime) val;
        vector.add(DATE_TIME_FORMAT.format(ldt));
      }
      //System.out.println(val.getClass().getSimpleName());
      //ZonedDateTime dt = ZonedDateTime.parse(String.valueOf(val), formatter);
      //vector.add(dt.toEpochSecond());
    }
  }

  @Override
  public AtomicVector build() {
    return vector.build();
  }
}
