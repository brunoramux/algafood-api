# Etapa 1: Build
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copia o projeto
COPY . .

# Gera o .jar
RUN ./mvnw -q -DskipTests package

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia somente o jar final
COPY --from=build /app/target/*.jar app.jar

# Porta da aplicação
EXPOSE 8080

# Executa a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]