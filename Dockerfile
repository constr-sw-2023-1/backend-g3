from openjdk:17-alpine
expose 8080
copy . .
run ./gradlew clean build
cmd ./gradlew bootRun


