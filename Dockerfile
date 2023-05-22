from openjdk:17-alpine
expose 8083
copy . .
run ./gradlew clean build -x test
cmd ./gradlew bootRun