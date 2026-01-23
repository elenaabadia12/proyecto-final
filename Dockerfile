FROM maven:3.9.8-eclipse-temurin-21
RUN mkdir -p /usr/src/proyecto
WORKDIR /usr/src/proyecto
COPY . .
RUN mvn clean package
CMD java -jar target/proyecto-0.0.1-SNAPSHOT-jar-with-dependencies.jar