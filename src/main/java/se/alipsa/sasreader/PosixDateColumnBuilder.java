package se.alipsa.sasreader;

import com.epam.parso.Column;
import org.renjin.sexp.AtomicVector;
import org.renjin.sexp.DoubleArrayVector;
import org.renjin.sexp.StringArrayVector;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

public class PosixDateColumnBuilder implements ColumnBuilder {

  private static final List<String> formats = Arrays.asList(
      "E8601DN", "E8601DT", "E8601DX", "E8601DZ", "E8601LX",
      "B8601DN", "B8601DT", "B8601DX", "B8601DZ", "B8601LX",
      "DATEAMPM", "DATETIME", "DTDATE", "DTMONYY",
      "DTWKDATX", "DTYEAR", "TOD", "MDYAMPM"
  );

  private final DoubleArrayVector.Builder vector = new DoubleArrayVector.Builder();

  private static final StringArrayVector classDefinition;
  private final ZoneOffset offset = OffsetDateTime.now().getOffset();

  static {
    StringArrayVector.Builder builder = StringArrayVector.newBuilder();
    builder.add("POSIXct");
    builder.add("POSIXt");
    classDefinition = builder.build();
  }

  public PosixDateColumnBuilder() {
    vector.setAttribute("class", classDefinition);
  }

  public static boolean acceptsType(Column column) {
    return "Date".equals(column.getType().getSimpleName()) || formats.contains(column.getFormat().getName());
  }

  @Override
  public void addValue(Object[] row, int columnIndex) {
    Object val = row[columnIndex];
    if (val == null) {
      vector.addNA();
    } else {
      if (!(val instanceof LocalDateTime)) {
        throw new IllegalArgumentException(val + " in column " + columnIndex + " is not a LocalDateTime");
      }
      LocalDateTime ldt = (LocalDateTime) val;
      vector.add(ldt.toEpochSecond(offset));

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
