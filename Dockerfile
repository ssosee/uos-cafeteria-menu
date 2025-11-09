FROM amazoncorretto:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV TZ Asia/Seoul
ENV APP_VERSION=v1.1.12

ENTRYPOINT ["java", \
 "-Xms128m", \
 "-Xmx384m", \
 "-jar", "/app.jar"]