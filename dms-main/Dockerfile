FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
RUN apk add --no-cache bash findutils gcompat
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Dorg.gradle.parallel=true -Dorg.gradle.configureondemand=true -Dorg.gradle.caching=true -Dorg.gradle.configuration-cache=true -Dorg.gradle.unsafe.configuration-cache=true -Dorg.gradle.unsafe.configuration-cache-problems=warn -Dorg.gradle.workers.max=4 -Dorg.gradle.logging.level=lifecycle -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:+UseStringDeduplication -XX:-UsePerfData -XX:+DisableExplicitGC -XX:MaxMetaspaceSize=512m -Dkotlin.compiler.execution.strategy=in-process -Dkotlin.incremental=false -Dkotlin.daemon.jvm.options=-Xmx1g -Dkotlin.parallel.tasks.in.project=true -Dfile.encoding=UTF-8 -Duser.country=US -Duser.language=en -Duser.timezone=UTC -Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom -Dorg.gradle.internal.http.connectionTimeout=60000 -Dorg.gradle.internal.http.socketTimeout=60000 -Dorg.gradle.internal.repository.max.tentatives=1 -Dorg.gradle.internal.repository.initial.backoff=500 -Dorg.gradle.internal.network.retry.max.times=2"
ENV GRADLE_USER_HOME="/cache/.gradle"
RUN mkdir -p /cache/.gradle
COPY ./build.gradle* ./settings.gradle* ./gradle.properties* ./gradlew* ./
COPY ./gradle gradle
RUN chmod +x gradlew && ./gradlew --version
RUN chmod +x gradlew && ./gradlew dependencies || true
COPY ./ .
RUN chmod +x gradlew && ./gradlew clean bootJar -x test && \
    rm -rf .gradle /app/.gradle /root/.kotlin /tmp/* /var/tmp/* /tmp/kotlin-daemon*.log* 2>/dev/null || true

FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache tzdata && addgroup -g 1001 appgroup && adduser -u 1001 -G appgroup -s /bin/sh -D appuser
WORKDIR /app

EXPOSE 8080
ENV TZ=Asia/Seoul JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:+UseStringDeduplication -XX:-UsePerfData"

HEALTHCHECK --interval=10s --timeout=5s --start-period=30s --retries=3 CMD curl -f http://localhost:8080/health || exit 1

COPY --from=build --chown=appuser:appgroup /app/dms-main/main-infrastructure/build/libs/*.jar /tmp/libs/
RUN find /tmp/libs -name "*.jar" ! -name "*-plain.jar" -exec cp {} /app/app.jar \; && rm -rf /tmp/libs && chown appuser:appgroup /app/app.jar
USER appuser
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]