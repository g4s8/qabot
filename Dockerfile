FROM g4s8/jdk-11:alpine3.10 as build
MAINTAINER Kirill <g4s8.public@gmail.com>
LABEL Description="qabot server image"
WORKDIR /build
COPY src ./src/
COPY pom.xml LICENSE README.md ./
RUN mvn -B --quiet package

FROM g4s8/jre-11:alpine3.10
WORKDIR /app
COPY --from=build /build/target/qabot-jar-with-dependencies.jar /app/qabot.jar
ENTRYPOINT []
CMD java -jar qabot.jar -Dfile.encoding=UTF-8 --port=${PORT}
