## What is this

* Simple example code of using Spring boot and REST API. 
* The REST API is able to:
  * GET/PUT/POST/DELETE connection info and persist it into embedded database. 
  * Extract basic structure as catalogs, schemas, tables and columns.
  * Show records of any table.
  * Exception messages are hidden.

## Quick start

* At least jre 1.8 has to be installed.
* By default, port 8080 on localhost should be available.
* build with `./mvnw clean package`
* Execute with `java -jar target/dbviewer-server-0.0.1-SNAPSHOT.jar`
* Install `jq - commandline JSON processor` for better visibility of examples.

## Persistence
* H2 is used to store connection records.
* Persistence is done with Jpa and Hibernate as implementation.
* The embedded database is pre-populated by Liquibase.
* Files connections.mv.db and connections.trace.db are created and can be removed to start over. 

## Api examples with embedded connection database

* list connections

  ```bash
  curl -v -XGET 'http://localhost:8080/connections' | jq .
  ```

* create new connection

  ```bash
  curl -v -XPOST -H "Content-type: application/json" -d '{
    "name": "sample",
    "jdbcDriver": "sampleDriver",
    "jdbcUrl": "jdbc:sample.url",
    "username": "sa",
    "password": "sa"
  }' 'http://localhost:8080/connections' | jq .
  ```

* update pre-populated connection, password is optional

  ```bash
  curl -v -XPUT -H "Content-type: application/json" -d '{
      "id": "791c1583-f208-4bf3-ac4b-e31347c46111",
      "name": "mem h2",
      "jdbcDriver": "org.h2.Driver",
      "jdbcUrl": "jdbc:h2:mem:test",
      "username": "sa",
      "password": "differentPwd"
    }' 'http://localhost:8080/connections/791c1583-f208-4bf3-ac4b-e31347c46111' | jq .
  ```

* delete pre-populated connection

  ```bash
  curl -v -XDELETE 'http://localhost:8080/connections/791c1583-f208-4bf3-ac4b-e31347c46111'
  ```

* list catalogs

  ```bash
  curl -v -XGET 'http://localhost:8080/connections/b00002eb-07ac-4e9f-bdae-7e417f101861/catalogs' | jq .
  ```
  
* list schemas

  ```bash
  curl -v -XGET 'http://localhost:8080/connections/b00002eb-07ac-4e9f-bdae-7e417f101861/catalogs/CONNECTIONS/schemas' | jq .
  ```
  
* list tables

  ```bash
  curl -v -XGET 'http://localhost:8080/connections/b00002eb-07ac-4e9f-bdae-7e417f101861/catalogs/CONNECTIONS/schemas/PUBLIC/tables' | jq .
  ```
  
* list columns

  ```bash
  curl -v -XGET 'http://localhost:8080/connections/b00002eb-07ac-4e9f-bdae-7e417f101861/catalogs/CONNECTIONS/schemas/PUBLIC/tables/CONNECTION/columns' | jq .
  ```
  
* list records

  ```bash
  curl -v -XGET 'http://localhost:8080/connections/b00002eb-07ac-4e9f-bdae-7e417f101861/catalogs/CONNECTIONS/schemas/PUBLIC/tables/CONNECTION/records' | jq .
  ```
  
* list records with offset and limit

  ```bash
  curl -v -XGET 'http://localhost:8080/connections/b00002eb-07ac-4e9f-bdae-7e417f101861/catalogs/CONNECTIONS/schemas/PUBLIC/tables/CONNECTION/records?offset=2&limit=2' | jq .
  ```

* remote h2 instance example

  ```bash
  curl -v -XGET 'http://localhost:8080/connections/791c1583-f208-4bf3-ac4b-e31347c46115/catalogs/TEST/schemas' | jq .
  ```
  
* remote derby instance example, catalog can be anything as they are not supported, defult schema is equal to user

  ```bash
  curl -v -XGET 'http://localhost:8080/connections/791c1583-f208-4bf3-ac4b-e31347c46116/catalogs/mydb/schemas/SA/tables' | jq .
  ```

* remote derby instance example, read records, it does not support catalogs

  ```bash
  curl -v -XGET 'http://localhost:8080/connections/791c1583-f208-4bf3-ac4b-e31347c46116/catalogs/mydb/schemas/SA/tables/connection/records' | jq .
  ```
  
## Embedded tools
* [Swagger UI](http://localhost:8080/swagger-ui)
* [H2 Console](http://localhost:8080/h2-console) user: sa, password: sa