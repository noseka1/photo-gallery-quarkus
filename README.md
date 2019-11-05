# photo-gallery-quarkus

## Build

You can build this project as a Java application using:

```
mvn clean install package
```

To build a native executable, issue the command:

```
mvn clean install package -Pnative
```

Note that in order to build native binaries you will need to configure GraalVM on your build machine. You can follow the [instructions](https://quarkus.io/guides/building-native-image.html) provided by the Quarkus project.

## Database

This component requires access to a PostgreSQL database. You can create it using:

```
psql -c 'CREATE DATABASE gallery'
psql -c "CREATE USER gallery WITH ENCRYPTED PASSWORD 'password'"
psql -c 'GRANT ALL PRIVILEGES ON DATABASE gallery TO gallery'
```

## Run

You can run this application using:

```
java -jar ./target/photo-gallery-quarkus-1.0-SNAPSHOT-runner.jar
```

or if you built the native executable:

```
./target/photo-gallery-quarkus-1.0-SNAPSHOT-runner
```

## Testing the Application

After the service starts up you can test it using curl.

To create some photos:

```
curl -v -X POST -H 'Content-Type: application/json' --data '{"name":"Odie","category":"animals"}' localhost:8080/photos
curl -v -X POST -H 'Content-Type: application/json' --data '{"name":"Garfield","category":"animals"}' localhost:8080/photos
curl -v -X POST -H 'Content-Type: application/json' --data '{"name":"Empire state building","category":"buildings"}' localhost:8080/photos
```

To retrieve all created photos:

```
curl -v localhost:8080/photos
```

To add some likes to the photo with ID 2:

```
curl -v -X POST -H 'Content-Type: application/json' --data '{"id":2,"likes":5}' localhost:8080/likes
curl -v -X POST -H 'Content-Type: application/json' --data '{"id":2,"likes":2}' localhost:8080/likes

```

To retrieve likes received by all photos:

```
curl -v localhost:8080/likes
```

To retrieve all photos from a specific category ordered by the number of likes:

```
curl localhost:8080/query?category=animals
```

## Deploying to OpenShift

Create a new project if it doesn't exist:

```
oc new-project photo-gallery-quarkus
```

Deploy a PostgreSQL database:

```
oc new-app \
--template postgresql-persistent \
--param DATABASE_SERVICE_NAME=postgresql \
--param POSTGRESQL_USER=gallery \
--param POSTGRESQL_PASSWORD=password \
--param POSTGRESQL_DATABASE=gallery
```

Define a binary build (this will reuse the Go artifacts that were you built at the beginning):

```
oc new-build \
--name photo-gallery \
--binary \
--strategy docker
```

Correct the Dockerfile location in the build config:

```
oc patch bc photo-gallery -p '{"spec":{"strategy":{"dockerStrategy":{"dockerfilePath":"src/main/docker/Dockerfile.jvm"}}}}'
```

or if you want to build a Docker image for the native executable run this instead:

```
oc patch bc photo-gallery -p '{"spec":{"strategy":{"dockerStrategy":{"dockerfilePath":"src/main/docker/Dockerfile.native"}}}}'
```

Start the binary build:

```
oc start-build \
photo-gallery \
--from-dir . \
--follow
```

Deploy the application:

```
oc new-app \
--image-stream photo-gallery \
--name photo-gallery \
--env GALLERY_DB_HOST=postgresql
```

Expose the application to the outside world:

```
oc expose svc photo-gallery
```
