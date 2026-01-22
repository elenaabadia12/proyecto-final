# 1. Usamos Java 21 (coincide con tu versión de Eclipse y el pom.xml)
FROM maven:3.9.8-eclipse-temurin-21

# 2. Creamos una carpeta dentro de la nube para meter tu código
RUN mkdir -p /usr/src/proyecto
WORKDIR /usr/src/proyecto

# 3. Copiamos todos tus archivos (src, pom.xml, etc.) a esa carpeta
COPY . .

# 4. Ejecutamos Maven para compilar y crear el ejecutable (.jar)
RUN mvn clean package

# 5. Comando mágico: Arranca tu aplicación
# IMPORTANTE: Este nombre debe coincidir con el artifactId de tu pom.xml
CMD java -jar target/proyecto-0.0.1-SNAPSHOT-jar-with-dependencies.jar