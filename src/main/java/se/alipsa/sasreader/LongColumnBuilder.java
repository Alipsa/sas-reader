package se.alipsa.sasreader;

import com.epam.parso.Column;
import org.renjin.sexp.AtomicVector;
import org.renjin.sexp.DoubleArrayVector;

public class LongColumnBuilder implements ColumnBuilder {

  public DoubleArrayVector.Builder vector = new DoubleArrayVector.Builder();

  public static boolean acceptsType(Column column) {
    String columnType = column.getType().getSimpleName();
    return "Long".equals(columnType);
  }

  @Override
  public void addValue(Object[] row, int columnIndex) {
    Object val = row[columnIndex];
    if (val == null) {
      vector.addNA();
    } else {
      vector.add(Long.valueOf(String.valueOf(val)));
    }
  }

  @Override
  public AtomicVector build() {
    return vector.build();
  }
}
