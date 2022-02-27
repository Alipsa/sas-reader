package se.alipsa.sasreader;

import org.junit.jupiter.api.Test;
import org.renjin.sexp.DoubleArrayVector;
import org.renjin.sexp.IntArrayVector;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.StringArrayVector;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SasReaderTest {

  @Test
  public void testMixAndMissing() throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("mix_and_missing.sas7bdat").getFile());
    ListVector vector = SasReader.asDataFrame(file.getAbsolutePath());
    DoubleArrayVector col = (DoubleArrayVector)vector.get("x2");
    assertEquals(col.get(36), Double.NaN);
    assertEquals(col.get(37), 2.0);
  }

  @Test
  public void testMixDataOne() throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("mixed_data_one.sas7bdat").getFile());
    ListVector vector = SasReader.asDataFrame(file.getAbsolutePath());
    DoubleArrayVector col = (DoubleArrayVector)vector.get("x2");
    assertEquals(col.get(2), 1.066666);
    StringArrayVector sCol = (StringArrayVector)vector.get("x3");
    assertEquals(sCol.getElementAsString(7), "HHH");
  }

  @Test
  public void testDateFormats() throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("date_formats.sas7bdat").getFile());
    ListVector vector = SasReader.asDataFrame(file.getAbsolutePath());
    assertEquals(67, vector.length());
    for (int i = 0; i < 49; i++) {
      String date = vector.getElementAsString(i);
      //System.out.println(i + ". " + date + " = " + LocalDate.ofEpochDay(date));
      assertEquals(LocalDate.of(2017, 3, 14), LocalDate.parse(date), "index " + i);
    }
    for (int i = 49; i < 67; i++) {
      String date = vector.getElementAsString(i);
      assertEquals(LocalDateTime.of(2017, 3, 14,15,36, 56),
          LocalDateTime.parse(date, DateColumnBuilder.DATE_TIME_FORMAT),
          "index " + i + ". " + date + " = " + LocalDateTime.parse(date, DateColumnBuilder.DATE_TIME_FORMAT));
    }
  }
}
