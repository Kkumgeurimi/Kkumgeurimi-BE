# Build stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# 1. 먼저 Gradle wrapper & 설정 파일만 복사
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle gradle

# 2. 캐시 최적화: 의존성만 먼저 다운로드
#   → src 코드 바뀌어도 dependencies 는 캐싱됨
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle dependencies --no-daemon

# 3. 이후 소스 코드 복사
COPY src src

# 4. 빌드 (테스트 제외)
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle build --no-daemon -x test

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy built jar
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# JVM options (optional)
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:+UseStringDeduplication"

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
