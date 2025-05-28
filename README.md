
# Notification Service

Notification Service is a Spring Boot-based microservice designed to handle notifications. It integrates with various components such as a PostgreSQL database, email services, and Eureka for service discovery.

## Features

- **Spring Boot**: Built with Spring Boot 3.4.4.
- **Database**: Uses PostgreSQL for data persistence.
- **Email Notifications**: Sends email notifications using Spring Boot Mail.
- **Service Discovery**: Integrates with Netflix Eureka for service registration and discovery.
- **Code Quality**: Enforces code quality with Checkstyle and JaCoCo for test coverage.

## Requirements

- **Java**: 21 or higher
- **Maven**: 3.8.0 or higher
- **PostgreSQL**: Installed and running
- **Eureka Server**: Running for service registration

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/PhumlaniDev/notification-service.git
cd notification-service
```

### Build the Project

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

## Configuration

### Database

Update the `application.properties` or `application.yml` file with your PostgreSQL database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/notification_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Eureka

Ensure the Eureka server URL is configured in the `application.properties` or `application.yml`:

```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

## Testing

Run the tests using Maven:

```bash
mvn test
```

## Code Quality

### Checkstyle

The project uses Google Checkstyle rules. To validate code style:

```bash
mvn checkstyle:check
```

### Test Coverage

Generate a JaCoCo test coverage report:

```bash
mvn jacoco:report
```

## License

This project is licensed under the MIT License.

## Author

Developed by PhumlaniDev.