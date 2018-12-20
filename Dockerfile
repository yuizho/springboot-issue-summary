FROM openjdk:11-jdk
COPY build/libs/springboot-issue-summary-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar","/app.jar", "--spring.profiles.active=logcollect"]