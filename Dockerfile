FROM openjdk:17-jdk-slim

EXPOSE 8080
ENV TZ=Asia/Seoul

COPY ./dms-infrastructure/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]