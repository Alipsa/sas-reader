library('hamcrest')
library('se.alipsa:sas-reader')

test.dateFormats <- function() {
  df <- readSas7bdat("date_formats.sas7bdat")
  assertThat(ncol(df), equalTo(67))

  for (col in 1:49) {
    # str(df[1,col])
    assertThat(df[1,col], equalTo(as.Date("2017-03-14")))
  }
  for (col in 50:67) {
    # str(df$datetime[1])
    assertThat(df$datetime[1], equalTo(as.POSIXct("2017-03-14 15:36:56")))
  }
}

test.cattle <- function() {
  df <- readSas7bdat("cattle.sas7bdat")
  assertThat(ncol(df), equalTo(9))
  assertThat(nrow(df), equalTo(27))
  assertThat(df[1,1], equalTo(244))
  assertThat(df$R2[10], closeTo(16.4, 0.000001))
}
