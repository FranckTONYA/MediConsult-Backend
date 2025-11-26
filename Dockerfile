# --------- BUILD STAGE ---------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copier pom.xml et télécharger les dépendances en cache
COPY pom.xml .
RUN mvn -q dependency:go-offline

# Copier le reste du code
COPY src ./src

# Construire l'application en jar
RUN mvn -q package -DskipTests

# --------- RUN STAGE ---------
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copier le jar built
COPY --from=build /app/target/*.jar app.jar

# Render fournit PORT dans l'environnement
ENV PORT=8080
EXPOSE 8080

# Commande de lancement
ENTRYPOINT ["java", "-jar", "app.jar"]
