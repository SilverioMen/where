# Conteneur unique (Spaceship Hyperlift) : le backend Spring Boot sert aussi le frontend statique.

# --- Étape 1 : build du frontend (SPA) ---
# Même version majeure de Node que la CI (Vite 8 exige Node 20.19+ ou 22.12+).
FROM node:22.21-bookworm-slim AS frontend
WORKDIR /fe
COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci
COPY frontend/ ./
# Utilise .env.production (VITE_API_BASE_URL=/api/v1, même origine).
RUN npm run build

# --- Étape 2 : build du backend, frontend embarqué dans les ressources statiques ---
FROM maven:3.9-eclipse-temurin-21 AS backend
WORKDIR /app
COPY backend/pom.xml .
COPY backend/src ./src
COPY --from=frontend /fe/dist/ ./src/main/resources/static/
RUN mvn -q -B -DskipTests package

# --- Étape 3 : image de run (JRE 21), exécutée par un utilisateur non root ---
FROM eclipse-temurin:21-jre
RUN groupadd --system --gid 10001 app \
    && useradd --system --uid 10001 --gid app --create-home --shell /usr/sbin/nologin app
WORKDIR /app
COPY --from=backend /app/target/orientation-backend-0.1.0.jar app.jar
USER app
# Hyperlift fournit $PORT ; application.yml lit server.port=${PORT:8080}.
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
