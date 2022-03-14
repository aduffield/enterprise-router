FROM openjdk:11 as build

EXPOSE 8080

ADD /target/hd-router-1.0-SNAPSHOT.jar hd-router.jar

ENTRYPOINT ["java", "-jar", "hd-router.jar"]
