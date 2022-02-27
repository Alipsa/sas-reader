library('hamcrest')
library('se.alipsa:sas-reader')

test.dateFormats <- function() {
  df <- readSas7bdat("date_formats.sas7bdat")
  assertThat(ncol(df), equalTo(67))
  #str(df[1,1])
  assertThat(df[1,1], equalTo(as.POSIXct("2017-03-14 01:00:00")))
  #str(df$datetime[1])
  # TODO: check if the time in SAS is 16:36:56 or 15:36:56 as haven claims
  assertThat(df$datetime[1], equalTo(as.POSIXct("2017-03-14 16:36:56")))
}

test.cattle <- function() {
  df <- readSas7bdat("cattle.sas7bdat")
  assertThat(ncol(df), equalTo(9))
  assertThat(nrow(df), equalTo(27))
  assertThat(df[1,1], equalTo(244))
  assertThat(df$R2[10], closeTo(16.4, 0.000001))
}
