# 컴파일은 CD(prod-cd.yml)에서 테스트와 함께 끝내고, 이 이미지는 그 jar 만 담는다.

FROM eclipse-temurin:17-jre-alpine

RUN apk add --no-cache tzdata curl && \
    addgroup -g 1001 appgroup && \
    adduser -u 1001 -G appgroup -s /bin/sh -D appuser

WORKDIR /app

ENV TZ=Asia/Seoul
ENV JAVA_OPTS="\
    -XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:+UseG1GC \
    -XX:G1HeapRegionSize=16m \
    -XX:+UseStringDeduplication \
    -XX:-UsePerfData \
"

COPY --chown=appuser:appgroup app.jar /app/app.jar

USER appuser
EXPOSE 8081

# 전용 헬스 엔드포인트가 없어 HTTP 응답 수신 여부로 기동 판정 (curl exit 22 = 4xx/5xx 응답도 서버 기동으로 간주)
HEALTHCHECK --interval=10s --timeout=5s --start-period=30s --retries=3 CMD curl -sf -o /dev/null http://localhost:8081/ || [ $? -eq 22 ]
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
