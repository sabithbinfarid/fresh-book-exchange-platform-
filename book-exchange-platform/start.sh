#!/bin/bash

# Book Exchange Platform - Docker Compose Startup Script
# This script starts all services for the application
#changing this file for pull request test and workflow test
set -e

echo "================================"
echo "Book Exchange Platform"
echo "Docker Compose Setup"
echo "================================"
#need to check if docker and docker compose are installed before running the command
# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker Desktop."
    exit 1
fi

# Check if Docker Compose is installed (plugin or standalone)
if ! docker compose version &> /dev/null && ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose."
    exit 1
fi

# Check if .env file exists
if [ ! -f ".env" ]; then
    echo "⚠️  .env file not found. Creating from .env.example..."
    cp .env.example .env
    echo "✓ .env file created. Please review it if needed."
fi

echo ""
echo "Starting services..."
echo "Building Docker image (this may take a few minutes on first run)..."
docker compose up --build

echo ""
echo "================================"
echo "✓ Services started successfully!"
echo "================================"
echo ""
echo "Application: http://localhost:8080"
echo "Database: localhost:5432"
echo ""
echo "Default Admin:"
echo "  Email: admin@bookexchange.com"
echo "  Password: Admin@123"
