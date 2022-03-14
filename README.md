# Hastings Enterprise Router

### Set up

Use Azurite Azure Storage for local development

Get the Docker image. This one has an admin ui screen so it's pretty cool.

```docker pull mcr.microsoft.com/azure-storage/azurite```

To run:

```docker run -p 10000:10000 mcr.microsoft.com/azure-storage/azurite azurite-blob --blobHost 0.0.0.0 --blobPort 10000```

This will start the local Azure Storage instance listening on port 10000.


### Now launch the application

```mvn spring-boot:run```

To pass in configuration items use:

```java -jar target/enterprise-router-1.0-SNAPSHOT.jar --jwt.secret=helloworld"```
or
```mvn spring-boot:run -Dspring-boot.run.arguments="--jwt.secret=helloworld"```

Runs on http://localhost:8080/hdroute

Initial routerUsers:
admin routerUser u: **_hd-router-su@hddirect.com_** p: **_123_**

```
{
"username":"hd-router-su@hddirect.com",
"password":"123"
}
```

Some things:

### Testing

Integration tests use an embedded Docker instance, so you don't need the one above.

```mvn integration-test -Pit```

### Running

Run using the configured Azure Storage container
`mvn spring-boot:run -Dspring-boot.run.profiles=production`

Run using in-memory storage
`mvn spring-boot:run -Dspring-boot.run.profiles=dev
`
mvn spring-boot:run -Dspring-boot.run.arguments=--logging.level.com.hastings.router=DEBUG -Dspring-boot.run.profiles=production


