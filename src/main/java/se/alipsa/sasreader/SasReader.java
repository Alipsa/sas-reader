package se.alipsa.sasreader;

import com.epam.parso.CSVDataWriter;
import com.epam.parso.Column;
import com.epam.parso.SasFileReader;
import com.epam.parso.impl.CSVDataWriterImpl;
import com.epam.parso.impl.SasFileReaderImpl;
import org.renjin.eval.EvalException;
import org.renjin.primitives.sequence.IntSequence;
import org.renjin.primitives.vector.ConvertingStringVector;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.StringVector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.epam.parso.date.OutputDateType.JAVA_TEMPORAL;

public class SasReader {

  public static SasFileReader parse(InputStream is) {
    return new SasFileReaderImpl(is, null, JAVA_TEMPORAL);
  }

  public static String asCsv(File file, String delimiter, String rowDelimiter, Locale locale) throws IOException {
    if (file == null || !file.exists()) {
      throw new IllegalArgumentException("File does not exist");
    }
    if (delimiter == null) {
      delimiter = ",";
    }
    if (rowDelimiter == null) {
      rowDelimiter = "\n";
    }
    if (locale == null) {
      locale = Locale.US;
    }
    try (StringWriter writer = new StringWriter(); InputStream is = Files.newInputStream(file.toPath())) {
      SasFileReader reader = parse(is);
      CSVDataWriter csvDataWriter = new CSVDataWriterImpl(writer, delimiter, rowDelimiter, locale);
      long rowCount = reader.getSasFileProperties().getRowCount();
      for (int i = 0; i < rowCount; i++) {
        csvDataWriter.writeRow(reader.getColumns(), reader.readNext());
      }
      return writer.toString();
    }
  }

  public static ListVector asDataFrame(String fileName) throws IOException {
    if (fileName == null) {
      throw new IllegalArgumentException("FileName argument cannot be null");
    }
    Path path = Paths.get(fileName);
    if (! path.toFile().exists()) {
      ClassLoader classLoader = SasReader.class.getClassLoader();
      URL url = classLoader.getResource(fileName);
      if (url == null) {
        throw new IllegalArgumentException("File " + fileName + " does not exist");
      }
      File file = new File(url.getFile());
      if (!file.exists()) {
        throw new IllegalArgumentException("File " + fileName + " does not exist");
      }
      path = file.toPath();
    }
    try (InputStream is = Files.newInputStream(path)) {
      SasFileReader reader = parse(is);
      List<Column> columns = reader.getColumns();
      long rowCount = reader.getSasFileProperties().getRowCount();
      int numColumns = columns.size();
      List<ColumnBuilder> builders = new ArrayList<>();
      for (Column column : columns) {
        if (DateColumnBuilder.acceptsType(column)) {
          builders.add(new DateColumnBuilder());
        } else if (DoubleColumnBuilder.acceptsType(column)) {
          builders.add(new DoubleColumnBuilder());
        } else if (StringColumnBuilder.acceptsType(column)) {
          builders.add(new StringColumnBuilder());
        } else if (LongColumnBuilder.acceptsType(column)) {
          builders.add(new LongColumnBuilder());
        } else {
          throw new EvalException("Unknown column type " + column);
        }
      }

      /* collect values */
      for (long i = 0; i < rowCount; i++) {
        Object[] row = reader.readNext();
        for (int j = 0; j < numColumns; j++) {
          builders.get(j).addValue(row, j);
        }
      }
      /* call build() on each column and add them as named cols to df */
      ListVector.NamedBuilder dataFrame = new ListVector.NamedBuilder();
      for (int i = 0; i < numColumns; i++) {
        Column column = columns.get(i);
        dataFrame.add(column.getName(), builders.get(i).build());
      }

      dataFrame.setAttribute("row.names", new ConvertingStringVector(IntSequence.fromTo(1, rowCount)));
      dataFrame.setAttribute("class", StringVector.valueOf("data.frame"));
      return dataFrame.build();

    }
  }

}
