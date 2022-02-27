package se.alipsa.sasreader;

import com.epam.parso.Column;
import org.renjin.sexp.AtomicVector;
import org.renjin.sexp.StringArrayVector;
import org.renjin.sexp.StringVector;

public class StringColumnBuilder implements ColumnBuilder {

  private final StringArrayVector.Builder vector = new StringVector.Builder();

  public static boolean acceptsType(Column column) {
    String columnType = column.getType().getSimpleName();
    return "String".equals(columnType);
  }

  @Override
  public void addValue(Object[] row, int columnIndex) {
    Object val = row[columnIndex];
    if (val == null) {
      vector.addNA();
    } else {
      vector.add(String.valueOf(val));
    }
  }

  @Override
  public AtomicVector build() {
    return vector.build();
  }
}
