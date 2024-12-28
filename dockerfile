FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} dobby.jar
ENV SPRING_PROFILES_ACTIVE=dev
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=dev", "/dobby.jar"]
