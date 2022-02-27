# remember to add export(function name) to NAMESPACE to make them available

readSas7bdat <- function(fileName) {
  SasReader$asDataFrame(fileName)
}