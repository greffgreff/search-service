FROM openjdk:17
ADD target/searchservice.jar searchservice.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/searchservice.jar"]