FROM java:8
VOLUME /tmp
ADD service-notify.jar /app.jar
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN bash -c 'touch /app.jar'
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Duser.timezone=GMT+08","/app.jar"]