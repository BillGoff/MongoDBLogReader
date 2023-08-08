# MongoDBLogReader
Java Application used to further read the MongoDBLogs, looking for long running queries.  It produces a fairly nice static HTML page, using JQuery Datatables.

## Build 
Application can be built via Maven.

```
mvn clean package
```
## Run Help.
Application comes with the following Command help.

```
java -jar ~/git/MongoDBLogReader/target/MongoDBLogReader*.jar -h
usage: help
 -f,--file <arg>     Log file to read
 -h,--help           will print out the command line options.
 -o,--output <arg>   "html" output file
```

## Run Application against a MongoDB Logfile.
```
java -jar ~/git/MongoDBLogReader/target/MongoDBLogReader*.jar -f <mongodbLogfile> -o <htmlOutputFile>
```

Example:  an HTML file named "mongodb.4Aug2023.html" will be produced from the "mongodb.log" file.
```
java -jar ~/git/MongoDBLogReader/target/MongoDBLogReader*.jar -f mongodb.log -o mongodb.4Aug2023
```

