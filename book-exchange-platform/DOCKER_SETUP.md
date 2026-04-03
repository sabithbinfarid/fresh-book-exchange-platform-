# Docker Setup Guide

This guide explains how to build and run the Community Library Platform using Docker.

## Prerequisites
- Docker Desktop installed (or Docker Engine on Linux, Mac)
- Docker Compose installed
- Git for version control

## Configuration

### 1. Environment Variables
Copy `.env.example` to `.env` (it should already be present):
```bash
# For Windows
copy .env.example .env

# For Linux/Mac
cp .env.example .env
```

The `.env` file contains:
- Database credentials
- Spring Boot profile configuration
- Application port

### 2. Project Structure
```
.
├── Dockerfile            # Multi-stage build for production
├── docker-compose.yml    # Orchestration of app + PostgreSQL
├── .env                  # Environment variables
├── .env.example          # Template for .env
├── pom.xml              # Maven build configuration
└── src/                 # Source code
    └── main/resources/
        ├── application.yml              # Default config
        ├── application-docker.yml       # Docker-specific config
        └── application-test.yml         # Test config
```

## Running the Application

### Option 1: Docker Compose (Recommended)
```bash
# Build and start all services
docker compose up --build

# The application will be available at: http://localhost:8080
```

### Option 2: Run Existing Images
If images are already built:
```bash
docker compose up
```

### Option 3: Individual Build
```bash
# Build Docker image
docker build -t book-exchange-platform:latest .

# Run with PostgreSQL
docker compose up
```

## Accessing the Application

1. **Web Interface**: http://localhost:8080
2. **Default Admin Account**:
   - Email: `admin@bookexchange.com`
   - Password: `Admin@123`
3. **Database (pgAdmin optional)**: 
   - Host: localhost
   - Port: 5432
   - Database: bookexchange
   - User: postgres
   - Password: postgres

## Stopping the Application
```bash
# Stop all services
docker compose down

# Stop and remove volumes
docker compose down -v
```

## Troubleshooting

### Port Already in Use
If port 8080 or 5432 is already in use:
1. Update `.env` with different ports
2. Modify `docker-compose.yml` port mappings
3. Or kill the existing process

### Database Connection Issues
- Ensure PostgreSQL service is healthy: `docker compose ps`
- Check logs: `docker compose logs postgres`

### Application Startup Issues
```bash
# View logs
docker compose logs app

# Watch logs in real-time
docker compose logs -f app
```

### Clean Rebuild
```bash
# Remove all containers and volumes
docker compose down -v

# Rebuild and start fresh
docker compose up --build
```

## Development Tips

### rebuild on Code Changes
```bash
# Rebuild and restart
docker compose up --build
```

### Access Database Shell
```bash
docker exec -it book-exchange-db psql -U postgres -d bookexchange
```

### View Docker Images
```bash
docker images | grep book-exchange
```

### Remove Old Images
```bash
docker rmi book-exchange-platform:latest
```

## Production Considerations
- Use strong passwords (update `.env`)
- Set `SPRING_JPA_HIBERNATE_DDL_AUTO=validate` instead of `update`
- Configure a reverse proxy (Nginx)
- Enable HTTPS
- Set up regular database backups
- Use Docker secrets for sensitive data
