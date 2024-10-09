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
 -d,--diff           (Optional) flag used to do a diff between log files.
 -f,--file <arg>     Log file to read
 -h,--help           will print out the command line options.
 -o,--output <arg>   (Optional) flag to set the output file name, without the "html"
 -v,--verbose        (Optional) flag that will produce a verbose output (Not a summary)
```

## Run Application against a MongoDB Logfile.
```
java -jar ~/git/MongoDBLogReader/target/MongoDBLogReader*.jar -f <mongodbLogfile> -o <htmlOutputFile>
```

Example:  an HTML file named "mongodb.4Aug2023.html" will be produced from the "mongodb.log" file.
```
java -jar ~/git/MongoDBLogReader/target/MongoDBLogReader*.jar -f mongodb.log -o mongodb.4Aug2023
```

## Run Application with the diff flag against two MongoDB Logfiles.
```
java -jar ~/git/MongoDBLogReader/target/MongoDBLogReader*.jar -d -f <mongodbLogfile> -g <mongodbLogfile> -o <htmlOutputFile>
```

Example:  an HTML file named "mongodb.4Aug2023.html" will be produced from the "mongodb.log" file.
```
java -jar ~/git/MongoDBLogReader/target/MongoDBLogReader*.jar -d -f mongodb.log -g mongodb2.logfile -o diff.mongodb.4Aug2023
```

## Run Application with the verbose flag against a MongoDB Logfile.
```
java -jar ~/git/MongoDBLogReader/target/MongoDBLogReader*.jar -v -f <mongodbLogfile> -o <htmlOutputFile>
```

Example:  an HTML file named "mongodb.4Aug2023.html" will be produced from the "mongodb.log" file.
```
java -jar ~/git/MongoDBLogReader/target/MongoDBLogReader*.jar -v -f mongodb.log -o mongodb.4Aug2023
```