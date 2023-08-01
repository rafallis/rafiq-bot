FROM openjdk:20-slim-buster

WORKDIR /app

RUN apt update && apt install uuid-dev tzdata -y

ENV TZ="America/Sao_Paulo"

ARG PROFILE

ENV PROJ_PROFILE=${PROFILE}

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=${PROJ_PROFILE}","-jar","/app/rafiq-bot.jar"]

VOLUME ["/app/config"]

COPY /target/rafiq-bot.jar .