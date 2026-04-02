@echo off
REM Book Exchange Platform - Docker Compose Startup Script (Windows)
REM This script starts all services for the application

setlocal enabledelayedexpansion

echo.
echo ================================
echo Book Exchange Platform

echo Docker Compose Setup
echo ================================
echo.

REM Check if Docker is installed
docker --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Docker is not installed. Please install Docker Desktop.
    pause
    exit /b 1
)

REM Check if .env file exists
if not exist ".env" (
    echo WARNING: .env file not found. Creating from .env.example...
    copy .env.example .env
    echo .env file created. Please review it if needed.
    echo.
)

echo Starting services...
echo Building Docker image (this may take a few minutes on first run)...
echo.

docker compose up --build

echo.
echo ================================
echo Services started successfully!
echo ================================
echo.
echo Application: http://localhost:8080
echo Database: localhost:5432
echo.
echo Default Admin:
echo   Email: admin@bookexchange.com
echo   Password: Admin@123
echo.
pause
