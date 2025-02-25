# Imagen base con Maven para compilar el código
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Crear un directorio de trabajo
WORKDIR /app

# Copiar los archivos del proyecto
COPY pom.xml .
COPY src ./src

# Compilar el proyecto
RUN mvn clean package -DskipTests

# Imagen final más liviana con solo el JAR
FROM eclipse-temurin:17-jdk-alpine

# Crear directorio de trabajo
WORKDIR /app

# Copiar el JAR generado desde el contenedor de compilación
COPY --from=builder /app/target/urbia-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto de la aplicación
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
