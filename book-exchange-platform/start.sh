#!/bin/bash

# Community Library Platform - Docker Compose Startup Script
# This script starts all services for the application
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "================================"
echo "Community Library Platform"
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
docker compose -f "$SCRIPT_DIR/docker-compose.yml" --env-file "$SCRIPT_DIR/.env" up --build

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
