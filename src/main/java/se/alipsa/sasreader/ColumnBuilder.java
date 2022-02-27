package se.alipsa.sasreader;

import org.renjin.sexp.AtomicVector;

public interface ColumnBuilder {

  void addValue(Object[] row, int columnIndex);

  AtomicVector build();
}
