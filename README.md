# Food Delivery Web Service
This project serves as the foundation of my food delivery web service.

## Getting Started

To get started with this project, follow these steps:

1. Clone the repository:

- [Download the latest release](https://github.com/Uyenntt71/food-delivery-ws/archive/refs/heads/main.zip)
- Clone the repo: `git clone https://git@github.com:Uyenntt71/food-delivery-ws.git`

2. Configure the project:

- Update the database connection details in the `application.properties` file located in the `src/main/resources` directory.

3. Build and run the application:

- Use your preferred Java IDE (e.g. IntelliJ) to import the project
- Build the project using Maven:
```
mvn clean package
```

This command will resolve dependencies, compile the source code, and create an executable JAR file in the `target` directory.

- Run the application:
```
java -jar target/food-delivery-ws.jar
```

The application should now be running on [http://localhost:8080](http://localhost:8080).

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, feel free to submit a pull request.
