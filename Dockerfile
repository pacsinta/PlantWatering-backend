FROM gradle:7-jdk18 AS builder
COPY src /home/gradle/src/src
COPY build.gradle.kts /home/gradle/src/build.gradle.kts
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon

FROM openjdk:18
EXPOSE 8001
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/MyPage.jar
COPY src/main/resources/pages /app/pages
CMD cd /app && java -jar MyPage.jar