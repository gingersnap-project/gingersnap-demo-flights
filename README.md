# Gingersnap Caching demo project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Dependencies

Run first

* Db Syncer
* Cache Manager
* MySQL with Docker Compose `docker-compose -f deploy/docker-compose.yaml up`

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Load Data in MySQL

* Load all the data with `curl http://localhost:8082/demo?reload=true&all=true`

## Config for Cache Manager

```properties
quarkus.banner.enabled=false
quarkus.package.type=uber-jar

quarkus.smallrye-openapi.info-title=Gingersnap API
quarkus.smallrye-openapi.info-contact-url=https://gingersnap-project.io
quarkus.smallrye-openapi.info-license-name=Apache 2.0
quarkus.smallrye-openapi.info-license-url=https://www.apache.org/licenses/LICENSE-2.0.html

quarkus.datasource.db-kind=MYSQL
quarkus.datasource.username=gingersnap_user
quarkus.datasource.password=password
quarkus.datasource.reactive.url=mysql://localhost:3306/airports

gingersnap.rule.us-country.key-type=PLAIN
gingersnap.rule.us-country.plain-separator=:
gingersnap.rule.us-country.select-statement=select name, continent from Country where isoCode = ?

gingersnap.rule.us-country.connector.schema=airports
gingersnap.rule.us-country.connector.table=Country

gingersnap.rule.us-airports.key-type=PLAIN
gingersnap.rule.us-airports.plain-separator=:
gingersnap.rule.us-airports.select-statement=select name, city from Airport where iata = ?

gingersnap.rule.us-airports.connector.schema=airports
gingersnap.rule.us-airports.connector.table=Airport

quarkus.devservices.enabled=false
quarkus.elasticsearch.health.enabled=false
```

## Config for DB Syncer
```properties
quarkus.banner.enabled=false
quarkus.package.type=uber-jar

# Connection configuration
%dev.gingersnap.database.type=MYSQL
%dev.gingersnap.database.host=localhost
%dev.gingersnap.database.port=3306
%dev.gingersnap.database.user=gingersnap_user
%dev.gingersnap.database.password=password
%dev.gingersnap.cache.uri=hotrod://127.0.0.1:11222

## Kubernetes Configuration
gingersnap.k8s.rule-config-map=
gingersnap.k8s.namespace=default

```