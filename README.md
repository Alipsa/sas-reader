# sas-reader
Renjin R package for reading sas7Bdat files into a data.frame.

## Usage
add the following dependency to your pom.xml 
```xml
<dependency>
    <groupId>se.alipsa</groupId>
    <artifactId>sas-reader</artifactId>
    <version>1.0.0</version>
</dependency>
```
Regular releases are available on maven central so no additional settings are required.
If you want to use a snapshot however, you need to add the following repository to your repositories section
```xml
    <repository>
        <id>snapshots-repo</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <releases><enabled>false</enabled></releases>
        <snapshots><enabled>true</enabled></snapshots>
    </repository>
```

Then in your R code simply load the package and use it as follows:
```r
library('se.alipsa:sas-reader')
df <- readSas7bdat(paste0(getwd(), "/cattle.sas7bdat"))
```

### Notes
Dates are converted to either `Date` or `POSIXct` depending on the format defined for the column in SAS.

## 3:rd Party dependencies

### org.renjin:renjin-script-engine
The R platform this package is for. GNU General Public License v2.0

### com.epam:parso
Parses sas7bdat files. Apache License 2.0

### org.slf4j:slf4j-api
Logging library. MIT Licence



