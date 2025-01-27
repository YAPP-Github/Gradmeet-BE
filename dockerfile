FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=build/libs/*.jar
ARG SPRING_PROFILES_ACTIVE=dev
COPY ${JAR_FILE} dobby.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "/dobby.jar"]
