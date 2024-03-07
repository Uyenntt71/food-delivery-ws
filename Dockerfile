FROM openjdk:11
WORKDIR /app
COPY target/food_delivery.jar .
EXPOSE 8080
CMD ["java", "-jar", "food_delivery.jar"]
