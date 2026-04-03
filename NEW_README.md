# Fresh Book Exchange Platform

A modern, full-featured book exchange platform built with Spring Boot and Java, allowing users to exchange books seamlessly.

## Features

- **User Authentication & Authorization** - Secure login and registration with role-based access control
- **Book Management** - Add, view, and manage book listings
- **Book Exchange Orders** - Create and manage book exchange orders
- **Admin Dashboard** - Administrative panel for managing users and platform activity
- **REST APIs** - Comprehensive REST APIs for integration
- **Docker Support** - Fully containerized application for easy deployment

## Technology Stack

- **Backend**: Spring Boot 3.x, Java 17+
- **Database**: PostgreSQL / MySQL (configurable)
- **Authentication**: JWT-based security
- **Frontend**: Thymeleaf templates
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose
- **Testing**: JUnit, Mockito

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- Docker & Docker Compose (optional)

### Local Development

1. Clone the repository:
```bash
git clone https://github.com/sabithbinfarid/fresh-book-exchange-platform.git
cd fresh-book-exchange-platform
```

2. Configure the application:
```bash
# Edit application.yml with your database credentials
```

3. Build and run:
```bash
./mvnw spring-boot:run
```

The application will be available at `http://localhost:8080`

### Docker Deployment

```bash
docker-compose up -d
```

## Project Structure

```
src/
├── main/java/com/example/bookexchange/
│   ├── config/          # Configuration classes
│   ├── controller/      # REST and View controllers
│   ├── dto/            # Data transfer objects
│   ├── entity/         # JPA entities
│   ├── exception/      # Exception handling
│   ├── repository/     # Data access layer
│   ├── security/       # Security configuration
│   └── service/        # Business logic
└── resources/
    ├── application.yml # Main application config
    └── templates/      # Thymeleaf HTML templates
```

## API Endpoints

- **Authentication**: `/api/auth/*`
- **Users**: `/api/users/*`
- **Books**: `/api/books/*`
- **Orders**: `/api/orders/*`

## Testing

Run tests with:
```bash
./mvnw test
```

## Contributing

1. Create a feature branch from `main` or `SubBranch`
2. Commit your changes
3. Push to the branch
4. Create a Pull Request

## License

This project is licensed under the MIT License.

## Support

For issues and questions, please open an issue on GitHub.
