FROM openjdk:11
WORKDIR /srv

COPY problem-report-0.0.1-SNAPSHOT.jar /srv/problem-report-0.0.1-SNAPSHOT.jar

CMD ["java","-Dspring.profiles.active=h2","-jar","/srv/problem-report-0.0.1-SNAPSHOT.jar"]
