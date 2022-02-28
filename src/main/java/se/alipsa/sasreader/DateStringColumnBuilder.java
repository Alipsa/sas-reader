package se.alipsa.sasreader;

import com.epam.parso.Column;
import org.renjin.sexp.AtomicVector;
import org.renjin.sexp.StringArrayVector;
import org.renjin.sexp.StringVector;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Fallback class, as far as the test data shows, DateColumnBuilder and PosixDateColumnBuilder covers all date types
 */
public class DateStringColumnBuilder implements ColumnBuilder {

  public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_DATE;
  public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private final StringArrayVector.Builder vector = new StringVector.Builder();

  public static boolean acceptsType(Column column) {
    // TODO: This is not tested, I don't have a test data file where this occurs
    return "Date".equals(column.getType().getSimpleName());
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
      } else if (val instanceof LocalDateTime){
        LocalDateTime ldt = (LocalDateTime) val;
        vector.add(DATE_TIME_FORMAT.format(ldt));
      } else {
        System.out.println(val.getClass().getSimpleName() + " : " + val);
        ZonedDateTime dt = ZonedDateTime.parse(String.valueOf(val));
        vector.add(dt.toEpochSecond());
      }
    }
  }

  @Override
  public AtomicVector build() {
    return vector.build();
  }
}
