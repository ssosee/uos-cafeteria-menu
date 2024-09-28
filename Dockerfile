FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV TZ Asia/Seoul
ENV APP_VERSION=v1.1.11
ENTRYPOINT ["java","-jar","/app.jar"]