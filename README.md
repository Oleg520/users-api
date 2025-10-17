# Users API - Microservices Architecture

A microservices-based user management system built with Spring Cloud, featuring Circuit Breaker pattern and centralized configuration.

## Architecture

- **API Gateway** (Port 8082) - Entry point with load balancing
- **User Service** (Port 8080) - User management with PostgreSQL
- **Notification Service** (Port 8081) - Email notifications via Kafka
- **Config Server** (Port 8888) - Centralized configuration management
- **Eureka Server** (Port 8761) - Service discovery and registration

## Features

- **Circuit Breaker** (Resilience4j) for fault tolerance
- **External Configuration** via Spring Cloud Config
- **Service Discovery** with Netflix Eureka
- **API Gateway** with load balancing
- **Event-driven architecture** with Apache Kafka
- **Health monitoring** with Spring Boot Actuator

## Quick Start

```bash
# Build and start all services
docker-compose up --build

# Check service health
curl http://localhost:8082/actuator/health
```

## API Endpoints

### User Management
- `POST /api/users` - Create user
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Monitoring
- `GET /actuator/health` - Service health
- `GET /actuator/circuitbreakers` - Circuit breaker status
- `GET /actuator/metrics` - Application metrics

## Configuration

All services use external configuration from Config Server. Local configuration files contain only environment-specific variables.

## Circuit Breaker

The system implements Circuit Breaker pattern for:
- User Service → Notification Service calls
- Notification Service → Email Service calls
- Gateway → Backend services

Configuration: 50% failure threshold, 30s timeout, 3 retry attempts with exponential backoff.

## Technology Stack

- Java 17
- Spring Boot 3.5.6
- Spring Cloud 2025.0.0
- PostgreSQL 16.1
- Apache Kafka 7.6.0
- Docker & Docker Compose
