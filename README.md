# sas-reader
Renjin R package for reading sas7Bdat files into a data.frame.

## Usage
add the following dependency to your pom.xml 
```xml
<dependency>
    <groupId>se.alipsa</groupId>
    <artifactId>sas-reader</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

If you want to use a snapshot, add the following repository to your repositories section
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
df <- readSas7bdat("cattle.sas7bdat")
```
