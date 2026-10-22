# ==============================================================================
# ETAPA 1: Construcción (Build Stage)
# ==============================================================================
# Usamos una imagen que incluye Maven 3.9 y JDK 21 sobre Alpine Linux (ultraligero).
# Le asignamos el alias "builder" para poder referenciarla en la siguiente etapa.
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

# Directorio de trabajo temporal dentro del contenedor para la compilación
WORKDIR /build

# Copiamos primero únicamente el descriptor de dependencias pom.xml.
# Truco de Docker Cache: Si no cambiaste dependencias en el pom.xml,
# Docker no volverá a descargar internet entero en los siguientes builds.
COPY pom.xml .

# Descarga todas las dependencias en modo batch (-B) y las almacena en la capa de caché.
RUN mvn dependency:go-offline -B

# Ahora sí copiamos el código fuente de nuestra aplicación
COPY src ./src

# Compilamos y empaquetamos el archivo .jar ejecutable en la carpeta /build/target/
# Omitimos los tests unitarios (-DskipTests) porque ya fueron verificados en el entorno local o CI.
RUN mvn clean package -DskipTests


# ==============================================================================
# ETAPA 2: Runtime de Producción (Run Stage)
# ==============================================================================
# Usamos ÚNICAMENTE el JRE (Java Runtime Environment) de Java 21 sobre Alpine.
# No contiene Maven ni compiladores. Esto reduce la imagen de ~800MB a menos de 200MB.
FROM eclipse-temurin:21-jre-alpine

# Directorio final de la aplicación en el contenedor de producción
WORKDIR /app

# Buena práctica de seguridad en Linux / Docker:
# Nunca ejecutar aplicaciones web como usuario "root". Creamos un usuario del sistema sin privilegios.
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copiamos desde la etapa "builder" ÚNICAMENTE el archivo .jar generado.
# Todo el código fuente, Maven y archivos intermedios quedan descartados.
COPY --from=builder /build/target/*.jar app.jar

# Documentamos el puerto en el que escucha nuestra API Spring Boot
EXPOSE 8081

# Comando definitivo e inmutable para arrancar el backend cuando nazca el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]
