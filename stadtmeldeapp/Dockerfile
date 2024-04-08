FROM maven:3.9.6-eclipse-temurin-21-jammy AS compiler
RUN mkdir /compile
WORKDIR /compile
COPY . .
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre
ARG profile
ENV profile ${profile}
WORKDIR /
COPY --from=compiler /compile/target/*.jar .
CMD ["sh", "-c", "java -jar -Dspring.profiles.active=${profile} $(ls *.jar)"]

