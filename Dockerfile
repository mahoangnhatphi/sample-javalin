# using multistage docker build
# ref: https://docs.docker.com/develop/develop-images/multistage-build/
    
# temp container to build using gradle
FROM gradle:5.3.0-jdk-alpine AS TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME
  
COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle ./src /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src
    
RUN gradle jar || return 0
COPY . .
RUN gradle clean jar
    
# actual container
FROM adoptopenjdk/openjdk11:alpine-jre
ENV APP_NAME=sample-javalin
ENV ARTIFACT_NAME=${APP_NAME}.jar
ENV APP_HOME=/usr/app/
    
WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/${APP_NAME}*.jar ${ARTIFACT_NAME}
    
EXPOSE 7000
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}
