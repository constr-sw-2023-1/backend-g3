FROM gradle:latest AS builder

WORKDIR /app

COPY . .

RUN gradle build --no-daemon

from openjdk:17-alpine

workdir /app

COPY --from=builder /app/build/libs/*.jar app.jar

cmd java -jar app.jar