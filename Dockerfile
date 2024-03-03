FROM eclipse-temurin:17-jre-focal

EXPOSE 8080
ENV TZ=Asia/Seoul

HEALTHCHECK --interval=5s --timeout=3s --retries=4 CMD curl -f http://localhost:8080 || exit 1

ARG PROFILE
ENV PROFILE ${PROFILE}

ARG MYSQL_HOST
ENV MYSQL_HOST ${MYSQL_HOST}

ARG MYSQL_USERNAME
ENV MYSQL_USERNAME ${MYSQL_USERNAME}

ARG MYSQL_PASSWORD
ENV MYSQL_PASSWORD ${MYSQL_PASSWORD}

ARG REDIS_HOST
ENV REDIS_HOST ${REDIS_HOST}

ARG SENTRY_DSN
ENV SENTRY_DSN ${SENTRY_DSN}

ARG AWS_ACCESS
ENV AWS_ACCESS ${AWS_ACCESS}

ARG AWS_SECRET
ENV AWS_SECRET ${AWS_SECRET}

ARG NEIS_KEY
ENV NEIS_KEY ${NEIS_KEY}

ARG SES_SENDER
ENV SES_SENDER ${SES_SENDER}

COPY ./dms-infrastructure/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
