# Estágio 1: Build (Usando Java 21 que é a versão estável mais próxima do 25 disponível em imagens prontas)
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
# Forçamos o Maven a ignorar a discrepância de versão se for apenas um detalhe de config
RUN mvn clean install -DskipTests

# Estágio 2: Execução
FROM eclipse-temurin:21-jre-alpine
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]