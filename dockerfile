FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=infrastructure/build/libs/*.jar
ARG SPRING_PROFILES_ACTIVE=dev
COPY ${JAR_FILE} dobby.jar
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "/dobby.jar"]
