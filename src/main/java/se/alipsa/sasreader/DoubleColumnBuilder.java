package se.alipsa.sasreader;

import com.epam.parso.Column;
import org.renjin.sexp.AtomicVector;
import org.renjin.sexp.DoubleArrayVector;

public class DoubleColumnBuilder implements ColumnBuilder {

  private final DoubleArrayVector.Builder vector = new DoubleArrayVector.Builder();

  public static boolean acceptsType(Column column) {
    String columnType = column.getType().getSimpleName();
    return "Number".equals(columnType) || "Double".equals(columnType);
  }

  @Override
  public void addValue(Object[] row, int columnIndex) {
    Object val = row[columnIndex];
    if (val == null) {
      vector.addNA();
    } else {
      vector.add(Double.parseDouble(String.valueOf(val)));
    }
  }

  @Override
  public AtomicVector build() {
    return vector.build();
  }
}
