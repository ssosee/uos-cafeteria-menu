FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV TZ Asia/Seoul
ENV APP_VERSION=v1.1.11
# ENTRYPOINT ["java","-jar","/app.jar"]
ENTRYPOINT ["java", \
 "-javaagent:/pinpoint-agent/pinpoint-bootstrap-2.5.3.jar", \
 "-Dpinpoint.agentId=aws-ec2-1", \
 "-Dpinpoint.applicationName=MyGarden", \
 "-jar", "/app.jar"]