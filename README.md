# code-with-quarkus Project

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

## TODO