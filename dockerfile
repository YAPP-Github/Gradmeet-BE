FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} dobby.jar
ENTRYPOINT ["java", "-jar", "/dobby.jar"]
