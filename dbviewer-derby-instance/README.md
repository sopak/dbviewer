## What is this

* The testing instance of in derby database with listening on localhost:1527

## Quick start

* At least jre 11 has to be installed.
* By default, port 8082 on localhost should be available.
* build with `./mvnw clean package`
* Execute with `java -jar target/dbviewer-derby-instance-0.0.1-SNAPSHOT.jar`

## Persistence
* The embedded mem database is pre-populated by Liquibase.