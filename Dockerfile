FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV TZ Asia/Seoul
ENV APP_VERSION=v1.0.8
ENTRYPOINT ["java","-jar","/app.jar"]