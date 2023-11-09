FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
ARG APP_VERSION=1.0.8
COPY ${JAR_FILE} app.jar
ENV TZ Asia/Seoul
ENV APP_VERSION=1.0.8
ENTRYPOINT ["java","-jar","/app.jar"]