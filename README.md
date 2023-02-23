# Gingersnap Caching demo project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

Demo doc: https://docs.google.com/document/d/1nGXKwvcn9Js_K3qIIeg6ZKA-hL9i1CDpTGJaE-ZFLos/edit

## Run

### Dependencies
* MySQL, cache-manager and db-syncer:
```
docker-compose -f deploy/docker-compose.yaml up
```

### Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Load Data in MySQL

* Load all the data 
```shell
http 'http://localhost:8082/demo?reload=true&all=true'
```

## Eager caching scenario

1. List all the departure flights of a day (1,2,3,4,5,6,7) to pick up a code or use `UX1098-2-D` for example. This is a database call
```shell
http http://localhost:8082/flights/departures/2 
```

2. Using the code flight, get the detail from the cache 
```shell
http 'http://localhost:8082/flights/cache/UX1098-2-D' 
```

3. Change the state of the flight (SCH, DEL, WIL, GTO, BRD, GCL, GTD, DEP, CNX, GCH, TOM)
```shell
http put 'localhost:8082/flights/UX1098-2-D?state=DEL' 
```

4. Get the detail from the cache again, and check how the cache was updated without any call
```shell
http 'http://localhost:8082/flights/cache/UX1098-2-D' 
```

## Deploying to Openshift
The Gingersnap Operator should already be installed in order for the CRs to be reconciled.

1. Deploy all dependencies:
```
./deploy/kuberenetes/dependencies.sh
```
2. Deploy the demo application:
```
./mvnw clean package -Dquarkus.profile=k8s  -Dquarkus.kubernetes.deploy=true -Dquarkus.container-image.build=false
kubectl -n demo port-forward svc/airports-demo 8080:80
```

In order to rebuild the container image, make sure to set `-Dquarkus.container-image.build=true`, otherwise the
existing `quay.io/gingersnap/airports-demo` image will be used.
