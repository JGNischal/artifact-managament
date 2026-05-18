FROM eclipse-temurin:23
WORKDIR /app

COPY out/artifacts/ArtifactManagement.jar ArtifactManagement.jar

CMD ["java", "-jar", "ArtifactManagement.jar"]