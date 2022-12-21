FROM eclipse-temurin:17-jre-focal

EXPOSE 8080
ENV TZ=Asia/Seoul

HEALTHCHECK --interval=5s --timeout=3s --retries=4 CMD curl -f http://localhost:8080 || exit 1

COPY ./dms-infrastructure/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]