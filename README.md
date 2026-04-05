# 📚 Fresh Book Exchange Platform

<div align="center">

[![Java](https://img.shields.io/badge/Java-17+-success?logo=java)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-success?logo=spring-boot)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue?logo=apache-maven)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue?logo=docker)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

A modern, full-featured **peer-to-peer book exchange platform** that enables users to discover, list, and exchange books seamlessly. Built with enterprise-grade technologies for reliability, security, and scalability.

[Quick Start](#-quick-start) • [Features](#-core-features) • [API Docs](#-api-documentation) • [Contributing](#-contributing)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Core Features](#-core-features)
- [Technology Stack](#️-technology-stack)
- [System Architecture](#-system-architecture)
- [Quick Start](#-quick-start)
  - [Prerequisites](#prerequisites)
  - [Local Development](#local-development)
  - [Docker Deployment](#docker-deployment)
- [Project Structure](#-project-structure)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Configuration](#-configuration)
- [Security](#-security)
- [Testing](#-testing)
- [Performance & Monitoring](#-performance--monitoring)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🎯 Overview

Fresh Book Exchange Platform is a comprehensive solution for managing peer-to-peer book exchanges. It provides a user-friendly interface for discovering available books, creating exchange requests, and managing transactions within a community-driven ecosystem.

**Key Highlights:**
- ✅ JWT-based authentication with role-based access control
- ✅ Real-time order management system
- ✅ Advanced search and filtering capabilities
- ✅ Admin dashboard with analytics
- ✅ RESTful API design for third-party integrations
- ✅ Docker-ready for cloud deployment
- ✅ Comprehensive test coverage
- ✅ Production-grade error handling

---

## ✨ Core Features

### 👤 User Management
- **Registration & Authentication** - Secure signup with email verification
- **Profile Management** - User profiles with avatar, bio, and ratings
- **Role-Based Access Control** - ADMIN, USER, and MODERATOR roles
- **Account Security** - Password hashing with bcrypt, JWT token management
- **User Ratings** - Community reputation system based on successful exchanges

### 📖 Book Management
- **Book Listings** - Create and manage book inventory with detailed descriptions
- **Book Categories** - Organize books by genre, author, and publishing year
- **Availability Status** - Track book status (AVAILABLE, EXCHANGED, RESERVED, UNAVAILABLE)
- **Image Upload** - Associate book covers with listings
- **Search & Filtering** - Advanced search with multiple filter options
- **User Library** - Personal book collection management

### 🔄 Exchange Management
- **Exchange Requests** - Create and manage book exchange proposals
- **Order Tracking** - Track exchange status from request to completion
- **Order History** - Complete transaction history for users
- **Exchange Notifications** - Real-time updates on exchange requests
- **Flexible Scheduling** - Coordinate exchange timing with other users

### 🛡️ Admin Dashboard
- **User Management** - Monitor and manage user accounts
- **Order Analytics** - View exchange statistics and trends
- **Content Moderation** - Review and moderate book listings
- **System Monitoring** - Track application health and performance
- **Reporting Tools** - Generate detailed reports on platform activity

### 🔌 Integration & APIs
- **RESTful APIs** - Comprehensive REST endpoints for all operations
- **OpenAPI/Swagger** - Interactive API documentation
- **Webhooks** - Event-driven architecture for integrations
- **Rate Limiting** - API throttling for fair usage
- **API Versioning** - Support for multiple API versions

---

## 🛠️ Technology Stack

| Layer | Technologies |
|-------|---------------|
| **Backend Framework** | Spring Boot 3.x, Spring Security, Spring Data JPA |
| **Language** | Java 17+ |
| **Build Tool** | Maven 3.8+ |
| **Database** | PostgreSQL (Primary), MySQL (Optional) |
| **Cache** | Redis (Optional) |
| **Authentication** | JWT (JSON Web Tokens) with Spring Security |
| **Frontend** | Thymeleaf, Bootstrap, HTML5, CSS3 |
| **API Documentation** | Springdoc OpenAPI (Swagger) |
| **Testing** | JUnit 5, Mockito, TestContainers |
| **Containerization** | Docker, Docker Compose |
| **CI/CD** | GitHub Actions |
| **Logging** | SLF4J with Logback |
| **Monitoring** | Spring Actuator |

---

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Client Layer                             │
│         (Web Browser, Mobile App, Third-party Clients)      │
└────────────────────────┬────────────────────────────────────┘
                         │
         ┌───────────────┴──────────────┐
         │                              │
    ┌────▼─────┐              ┌────────▼──────┐
    │ REST API │              │ View Layer    │
    │ (JSON)   │              │(Thymeleaf)    │
    └────┬─────┘              └────────┬──────┘
         │                              │
    ┌────▼──────────────────────────────▼──────┐
    │     Spring Security & JWT Auth Layer     │
    └────┬──────────────────────────────┬──────┘
         │                              │
    ┌────▼──────────────────────────────▼──────┐
    │      Service Layer (Business Logic)      │
    │ ┌──────────────────────────────────────┐ │
    │ │ BookService  │ OrderService          │ │
    │ │ UserService  │ NotificationService  │  │
    └─┼──────────────────────────────────────┼─┘
      │                                      │
    ┌─┴──────────────────────────────────────┴─┐
    │    Repository Layer (Data Access)        │
    │  ┌─────────────┐  ┌──────────────────┐   │
    │  │BookRepository│ │OrderRepository   │   │
    │  │UserRepository│ │RoleRepository    │   │
    └──┼─────────────┼──┼──────────────────┼──┘
       │             │  │                  │
    ┌──▼─────────────▼──▼──────────────────▼──┐
    │     PostgreSQL/MySQL Database           │
    │  ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐    │
    │  │Users │ │Books │ │Orders│ │Roles │    │
    └──┴──────┴─┴──────┴─┴──────┴─┴──────┴───┘
```

---
![Architecture Diagram](./diagram/arch.jpg)

![ER Diagram](./diagram/er.jpg)
## 🚀 Quick Start

### Prerequisites

Ensure you have the following installed:

- **Java 17 or higher** - [Download JDK](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.8+** - [Download Maven](https://maven.apache.org/download.cgi)
- **Git** - [Download Git](https://git-scm.com/downloads)
- **PostgreSQL 12+** (Optional) - [Download PostgreSQL](https://www.postgresql.org/download/)
- **Docker & Docker Compose** (Optional) - [Download Docker](https://www.docker.com/products/docker-desktop)

### Local Development

#### Step 1: Clone the Repository
```bash
git clone https://github.com/sabithbinfarid/fresh-book-exchange-platform.git
cd fresh-book-exchange-platform
```

#### Step 2: Configure Database
Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/book_exchange
    username: postgres
    password: your_password
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQL10Dialect
```

#### Step 3: Install Dependencies & Build
```bash
mvn clean install
```

#### Step 4: Run the Application
```bash
mvn spring-boot:run
```

The application will be available at: **http://localhost:8080**

#### Step 5: Access the Application

| Feature | URL |
|---------|-----|
| Home Page | http://localhost:8080 |
| Login | http://localhost:8080/login |
| Register | http://localhost:8080/register |
| Dashboard | http://localhost:8080/dashboard |
| Admin Panel | http://localhost:8080/admin |
| API Swagger | http://localhost:8080/swagger-ui.html |

### Docker Deployment

#### Step 1: Build Docker Image
```bash
docker build -t book-exchange-platform:latest .
```

#### Step 2: Run with Docker Compose
```bash
docker-compose up -d
```

#### Step 3: Verify Deployment
```bash
docker ps
docker logs -f book-exchange-app
```

The application will be available at: **http://localhost:8080**

#### Stop Services
```bash
docker-compose down
```

---

## 📁 Project Structure

```
book-exchange-platform/
│
├── src/
│   ├── main/
│   │   ├── java/com/example/bookexchange/
│   │   │   ├── BookExchangePlatformApplication.java    # Entry point
│   │   │   │
│   │   │   ├── config/                                 # Configuration
│   │   │   │   ├── DataSeeder.java                    # Initial data loader
│   │   │   │   └── SecurityConfig.java                # Spring Security config
│   │   │   │
│   │   │   ├── controller/                             # Controllers
│   │   │   │   ├── AuthController.java                # Authentication
│   │   │   │   ├── AuthRestController.java            # REST Auth endpoints
│   │   │   │   ├── BookRestController.java            # Book CRUD APIs
│   │   │   │   ├── OrderRestController.java           # Order APIs
│   │   │   │   ├── UserRestController.java            # User APIs
│   │   │   │   └── ViewController.java                # View controllers
│   │   │   │
│   │   │   ├── dto/                                    # Data Transfer Objects
│   │   │   │   ├── BookRequest.java
│   │   │   │   ├── BookResponse.java
│   │   │   │   ├── OrderRequest.java
│   │   │   │   ├── OrderResponse.java
│   │   │   │   ├── RegisterRequest.java
│   │   │   │   ├── UserResponse.java
│   │   │   │   └── UserUpdateRequest.java
│   │   │   │
│   │   │   ├── entity/                                 # JPA Entities
│   │   │   │   ├── Book.java
│   │   │   │   ├── BookOrder.java
│   │   │   │   ├── BookStatus.java
│   │   │   │   ├── OrderStatus.java
│   │   │   │   ├── Role.java
│   │   │   │   ├── RoleName.java
│   │   │   │   └── User.java
│   │   │   │
│   │   │   ├── exception/                              # Exception Handling
│   │   │   │   ├── ApiError.java
│   │   │   │   ├── BadRequestException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   │
│   │   │   ├── repository/                             # Data Access Layer
│   │   │   │   ├── BookOrderRepository.java
│   │   │   │   ├── BookRepository.java
│   │   │   │   ├── RoleRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   │
│   │   │   ├── security/                               # Security
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   │
│   │   │   └── service/                                # Business Logic
│   │   │       ├── BookService.java
│   │   │       ├── OrderService.java
│   │   │       ├── UserService.java
│   │   │       └── impl/
│   │   │           ├── BookServiceImpl.java
│   │   │           ├── OrderServiceImpl.java
│   │   │           └── UserServiceImpl.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml                         # Main config
│   │       ├── application-docker.yml                  # Docker config
│   │       ├── application-test.yml                    # Test config
│   │       └── templates/                              # Thymeleaf templates
│   │           ├── admin-users.html
│   │           ├── books.html
│   │           ├── dashboard.html
│   │           ├── login.html
│   │           ├── orders.html
│   │           └── register.html
│   │
│   └── test/
│       └── java/com/example/bookexchange/
│           ├── controller/                             # Controller Tests
│           │   ├── BookRestControllerIntegrationTest.java
│           │   ├── OrderRestControllerIntegrationTest.java
│           │   └── UserRestControllerIntegrationTest.java
│           └── service/                                # Service Tests
│               ├── BookServiceImplTest.java
│               ├── OrderServiceImplTest.java
│               └── UserServiceImplTest.java
│
├── Dockerfile                                          # Docker configuration
├── docker-compose.yml                                  # Docker Compose setup
├── pom.xml                                             # Maven dependencies
├── mvnw & mvnw.cmd                                     # Maven wrapper
├── README.md                                           # This file
└── .github/workflows/                                  # CI/CD pipelines

```

---

## 🔌 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### Register New User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePassword123!",
  "fullName": "John Doe"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "SecurePassword123!"
}

Response:
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "username": "john_doe",
  "roles": ["USER"]
}
```

### Book Endpoints

#### Get All Books
```http
GET /api/books?page=0&size=10
Authorization: Bearer {token}
```

#### Get Book by ID
```http
GET /api/books/{id}
Authorization: Bearer {token}
```

#### Create New Book
```http
POST /api/books
Content-Type: application/json
Authorization: Bearer {token}

{
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "isbn": "978-0-7432-7356-5",
  "genre": "Fiction",
  "description": "Classic American novel",
  "yearPublished": 1925
}
```

#### Update Book
```http
PUT /api/books/{id}
Content-Type: application/json
Authorization: Bearer {token}

{
  "title": "Updated Title",
  "genre": "Classic Fiction"
}
```

#### Delete Book
```http
DELETE /api/books/{id}
Authorization: Bearer {token}
```

### Order Endpoints

#### Create Exchange Order
```http
POST /api/orders
Content-Type: application/json
Authorization: Bearer {token}

{
  "bookId": 1,
  "requestedBookId": 2,
  "proposedDate": "2026-04-15T10:00:00"
}
```

#### Get User's Orders
```http
GET /api/orders/my-orders
Authorization: Bearer {token}
```

#### Update Order Status
```http
PUT /api/orders/{id}/status
Content-Type: application/json
Authorization: Bearer {token}

{
  "status": "ACCEPTED"
}
```

### User Endpoints

#### Get User Profile
```http
GET /api/users/profile
Authorization: Bearer {token}
```

#### Update Profile
```http
PUT /api/users/profile
Content-Type: application/json
Authorization: Bearer {token}

{
  "fullName": "John Doe",
  "bio": "Book lover",
  "location": "New York, USA"
}
```

#### Get User Statistics
```http
GET /api/users/{id}/stats
Authorization: Bearer {token}
```

For complete API documentation with interactive Swagger UI, visit:
**http://localhost:8080/swagger-ui.html**

---

## 🗄️ Database Schema

```sql
-- Users Table
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(100),
  bio TEXT,
  role_id INTEGER REFERENCES roles(id),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Books Table
CREATE TABLE books (
  id SERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(100),
  isbn VARCHAR(20) UNIQUE,
  genre VARCHAR(50),
  description TEXT,
  status VARCHAR(20) DEFAULT 'AVAILABLE',
  owner_id INTEGER REFERENCES users(id),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Book Orders Table
CREATE TABLE book_orders (
  id SERIAL PRIMARY KEY,
  book_id INTEGER REFERENCES books(id) NOT NULL,
  requested_book_id INTEGER REFERENCES books(id),
  requester_id INTEGER REFERENCES users(id) NOT NULL,
  status VARCHAR(20) DEFAULT 'PENDING',
  proposed_date TIMESTAMP,
  completed_date TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Roles Table
CREATE TABLE roles (
  id SERIAL PRIMARY KEY,
  name VARCHAR(20) UNIQUE NOT NULL
);
```

---

## ⚙️ Configuration

### Application Properties

#### Development Environment (`application.yml`)
```yaml
spring:
  application:
    name: book-exchange-platform
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  datasource:
    url: jdbc:postgresql://localhost:5432/book_exchange
  jwt:
    secret: your-secret-key-min-32-characters-long
    expiration: 86400000  # 24 hours in milliseconds
```

#### Production Environment (`application-prod.yml`)
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate
```

---

## 🔐 Security

### Authentication & Authorization
- **JWT Tokens** - Stateless authentication with JWT
- **Password Security** - BCrypt hashing with salt
- **Role-Based Access** - Admin, User, Moderator roles
- **CORS Configuration** - Cross-origin resource sharing
- **HTTPS Ready** - Supports SSL/TLS encryption

### Security Best Practices
```java
// Passwords are automatically hashed
new BCryptPasswordEncoder().encode(password);

// JWT token validation on every request
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...

// Role-based endpoint protection
@PreAuthorize("hasRole('ADMIN')")
```

---

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=BookServiceImplTest
```

### Run Tests with Coverage Report
```bash
mvn test jacoco:report
# Report location: target/site/jacoco/index.html
```

### Test Suite Overview

| Test Class | Coverage | Purpose |
|-----------|----------|---------|
| `BookRestControllerIntegrationTest` | Integration | REST controller testing |
| `OrderRestControllerIntegrationTest` | Integration | Order API testing |
| `UserRestControllerIntegrationTest` | Integration | User API testing |
| `BookServiceImplTest` | Unit | Business logic validation |
| `OrderServiceImplTest` | Unit | Order processing |
| `UserServiceImplTest` | Unit | User management |

---

## 📊 Performance & Monitoring

### Application Metrics
Access health and metrics at:
```
http://localhost:8080/actuator
http://localhost:8080/actuator/metrics
http://localhost:8080/actuator/health
```

### Database Performance Tips
- Use pagination for large result sets
- Enable query caching for frequently accessed data
- Create indexes on frequently searched columns

### Optimization Strategies
```java
// Use pagination
Page<Book> books = bookRepository.findAll(PageRequest.of(0, 20));

// Lazy load relationships
@ManyToOne(fetch = FetchType.LAZY)

// Use native queries for complex operations
@Query(value = "SELECT * FROM books WHERE...", nativeQuery = true)
```

---

## 🐛 Troubleshooting

### Common Issues & Solutions

#### 1. Database Connection Error
```
Error: org.postgresql.util.PSQLException: Connection refused
```
**Solution:** 
- Verify PostgreSQL is running: `sudo systemctl status postgresql`
- Check credentials in `application.yml`
- Ensure database exists: `createdb book_exchange`

#### 2. Maven Build Failure
```
[ERROR] COMPILATION ERROR
```
**Solution:**
- Clear Maven cache: `mvn clean`
- Update dependencies: `mvn -U clean install`
- Verify Java version: `java -version`

#### 3. JWT Token Expired
```
{"error": "Unauthorized", "message": "Token expired"}
```
**Solution:**
- Request new token by logging in again
- Increase expiration time in `application.yml`

#### 4. Docker Build Issues
```
docker: command not found
```
**Solution:**
- Install Docker Desktop: [Download](https://www.docker.com/products/docker-desktop)
- Start Docker service on Windows: `Start-Service Docker`

#### 5. Port Already in Use
```
Address already in use: bind failed on port 8080
```
**Solution:**
```bash
# Find process using port 8080
netstat -ano | findstr :8080

# Kill process (replace PID)
taskkill /PID <PID> /F

# Or change port in application.yml
server:
  port: 8081
```

---

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

### Development Workflow

1. **Fork the Repository**
   ```bash
   git clone https://github.com/sabithbinfarid/fresh-book-exchange-platform.git
   cd fresh-book-exchange-platform
   ```

2. **Create Feature Branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```

3. **Make Changes**
   - Follow existing code style
   - Write meaningful commit messages
   - Add tests for new features

4. **Commit Changes**
   ```bash
   git add .
   git commit -m "feat: Add amazing feature"
   ```

5. **Push to Branch**
   ```bash
   git push origin feature/amazing-feature
   ```

6. **Create Pull Request**
   - Go to GitHub and create a PR
   - Reference any related issues
   - Describe changes clearly

### Code Style Guidelines

```java
// Use meaningful variable names
String userEmail = "user@example.com";

// Add JavaDoc for public methods
/**
 * Creates a new book listing
 * @param request - Book details
 * @return Created book entity
 */
public Book createBook(BookRequest request) { ... }

// Handle exceptions properly
try {
  // Code
} catch (ResourceNotFoundException e) {
  logger.error("Resource not found", e);
  throw new ApiException("Error message");
}
```

### Commit Message Convention
```
feat: Add new feature
fix: Fix bug in module
docs: Update documentation
test: Add unit tests
refactor: Refactor code structure
style: Code style improvements
```

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2026 Fresh Book Exchange Platform

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction...
```

---

##  Support & Contact

- **Issues**: [GitHub Issues](https://github.com/sabithbinfarid/fresh-book-exchange-platform/issues)
- **Email**: support@bookexchangeplatform.com
- **Documentation**: [Wiki](https://github.com/sabithbinfarid/fresh-book-exchange-platform/wiki)
- **Discussions**: [GitHub Discussions](https://github.com/sabithbinfarid/fresh-book-exchange-platform/discussions)

---

## Acknowledgments

- Spring Boot documentation and community
- PostgreSQL for reliable database
- Docker for containerization
- All contributors and supporters

---

<div align="center">



[⬆ Back to Top](#-fresh-book-exchange-platform)

</div>
