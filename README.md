# Coffee Order Service

A microservice for managing coffee orders in a coffee shop system. This service handles the creation, management, and tracking of coffee orders.

## Features

- RESTful API for coffee order management
- Secure authentication and authorization
- PostgreSQL database for data persistence
- Docker support for easy deployment
- API documentation with OpenAPI/Swagger
- Monitoring with Spring Actuator
- Caching with Caffeine
- Global exception handling
- Comprehensive logging

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Docker and Docker Compose
- PostgreSQL (if running locally)

## Environment Variables

The following environment variables need to be set:

```bash
# Database Configuration
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
DB_URL=jdbc:postgresql://localhost:5432/coffee-app

# Admin Credentials
ADMIN_USERNAME=admin_username
ADMIN_PASSWORD=admin_password

# Application Configuration
SERVER_PORT=8081
```

## Getting Started

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd coffee-order-service
   ```

2. Set up environment variables:
   ```bash
   # Linux/Mac
   export DB_USERNAME=postgres
   export DB_PASSWORD=your_password
   
   # Windows
   set DB_USERNAME=postgres
   set DB_PASSWORD=your_password
   ```

3. Run with Docker Compose:
   ```bash
   docker-compose up -d
   ```

   Or run locally with Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

## API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: http://localhost:8081/coffee-order-service/swagger-ui.html
- OpenAPI Spec: http://localhost:8081/coffee-order-service/v3/api-docs

## Monitoring

Health and metrics endpoints are available at:
- Health Check: http://localhost:8081/coffee-order-service/actuator/health
- Metrics: http://localhost:8081/coffee-order-service/actuator/metrics

## Security

The application uses Basic Authentication for securing endpoints. The actuator endpoints are protected and only accessible by users with ADMIN role.

## Testing

Run the tests using:
```bash
./mvnw test
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.