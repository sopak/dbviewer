## What is this

* The testing instance of in memory h2 database with listening on localost:9090

## Quick start

* At least jre 1.8 has to be installed.
* By default, port 8080 on localhost should be available.
* build with `./mvnw clean package`
* Execute with `java -jar target/dbviewer-h2-instance-0.0.1-SNAPSHOT.jar`

## Persistence
* The embedded mem database is pre-populated by Liquibase.
  
## Embedded tools
* [H2 Console](http://localhost:8081/h2-console) user: sa, password: sa