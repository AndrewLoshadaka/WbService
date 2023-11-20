FROM openjdk:17
WORKDIR /app
COPY target/sybase.jar .
ENV CLASSPATH=/home/oleg/IdeaProjects/wb-service/stat-service/sybase-service/lib/jconn2.jar
CMD ["java", "-jar", "sybase.jar"]
