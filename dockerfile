FROM openjdk:17-ea-17-jdk-oracle

# Directorio de trabajo estandarizado
WORKDIR /app


# Copiamos el artefacto.
# Nota: Asegúrate de que el JAR esté en la misma carpeta que este Dockerfile.
COPY ./target/MDSERP-1.0.0-SNAPSHOT.jar app.jar

# Configuración de memoria dinámica para contenedores
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# La imagen siempre arranca con el perfil de producción (application.properties usa dev por defecto)
ENV SPRING_PROFILES_ACTIVE=prod

# Exponer el puerto estándar de Spring Boot
EXPOSE 8181

# Ejecución usando el comando optimizado
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]