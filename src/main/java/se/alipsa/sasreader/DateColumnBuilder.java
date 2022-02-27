package se.alipsa.sasreader;

import com.epam.parso.Column;
import org.renjin.sexp.AtomicVector;
import org.renjin.sexp.IntArrayVector;
import org.renjin.sexp.StringArrayVector;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
  //Tue Mar 14 01:00:00 CET 2017
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");

  private final IntArrayVector.Builder vector = new IntArrayVector.Builder();

  private static final StringArrayVector classDefinition;

  static {
    StringArrayVector.Builder builder = StringArrayVector.newBuilder();
    builder.add("POSIXct");
    builder.add("POSIXt");
    classDefinition = builder.build();
  }

  public DateColumnBuilder() {
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
      ZonedDateTime dt = ZonedDateTime.parse(String.valueOf(val), formatter);
      vector.add(dt.toEpochSecond());
    }
  }

  @Override
  public AtomicVector build() {
    return vector.build();
  }
}
