FROM java:8
VOLUME /tmp
ADD service-engine.jar /app.jar
RUN bash -c 'touch /app.jar'
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]