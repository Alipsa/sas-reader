package se.alipsa.sasreader;

import com.epam.parso.Column;
import org.renjin.sexp.AtomicVector;
import org.renjin.sexp.IntArrayVector;
import org.renjin.sexp.StringArrayVector;

import java.time.*;
import java.util.Arrays;
import java.util.List;

public class DateColumnBuilder implements ColumnBuilder {

  private static final List<String> formats = Arrays.asList(
      "B8601DA", "E8601DA", "DATE", "DAY", "DDMMYY", "DDMMYYB", "DDMMYYC", "DDMMYYD", "DDMMYYN", "DDMMYYP",
      "DDMMYYS", "WEEKDATE", "WEEKDATX", "WEEKDAY", "DOWNAME", "WORDDATE", "WORDDATX", "YYMM", "YYMMC", "YYMMD",
      "YYMMN", "YYMMP", "YYMMS", "YYMMDD", "YYMMDDB", "YYMMDDC", "YYMMDDD", "YYMMDDN", "YYMMDDP", "YYMMDDS", "YYMON",
      "YEAR", "JULDAY", "JULIAN", "MMDDYY", "MMDDYYC", "MMDDYYD", "MMDDYYN", "MMDDYYP", "MMDDYYS", "MMYY", "MMYYC",
      "MMYYD", "MMYYN", "MMYYP", "MMYYS", "MONNAME", "MONTH", "MONYY"
  );

  private final IntArrayVector.Builder vector = new IntArrayVector.Builder();

  private static final StringArrayVector classDefinition;

  static {
    StringArrayVector.Builder builder = StringArrayVector.newBuilder();
    builder.add("Date");
    classDefinition = builder.build();
  }

  public DateColumnBuilder() {
    vector.setAttribute("class", classDefinition);
  }


  public static boolean acceptsType(Column column) {
    return formats.contains(column.getFormat().getName());
  }

  @Override
  public void addValue(Object[] row, int columnIndex) {
    Object val = row[columnIndex];
    if (val == null) {
      vector.addNA();
    } else {
      if (! (val instanceof LocalDate)) {
        throw new IllegalArgumentException(val + " in column " + columnIndex + " is not a localdate");
      }
      LocalDate ld = (LocalDate) val;
      vector.add(ld.toEpochDay());
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
