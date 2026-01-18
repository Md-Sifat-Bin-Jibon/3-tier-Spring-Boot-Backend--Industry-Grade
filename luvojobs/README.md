# Luvojobs - Job Portal API

A RESTful API for a job portal application built with Spring Boot, featuring user authentication, JWT tokens, and PostgreSQL database integration.

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Security](#security)
- [Database](#database)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Environment Variables](#environment-variables)

## ✨ Features

- **User Authentication**
  - User signup with email and password
  - User login with JWT token generation
  - Automatic username generation (16-character alphanumeric)

- **Security**
  - Password hashing using BCrypt
  - JWT access tokens (24 hours expiration)
  - JWT refresh tokens (7 days expiration)
  - Spring Security integration

- **API Documentation**
  - Swagger/OpenAPI 3.0 documentation
  - Interactive API testing interface

- **Database**
  - PostgreSQL integration
  - JPA/Hibernate ORM
  - Automatic schema updates

## 🛠 Technology Stack

- **Framework**: Spring Boot 4.0.1
- **Language**: Java 25
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **Security**: Spring Security + JWT
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **ORM**: Spring Data JPA / Hibernate
- **Validation**: Jakarta Validation

## 🏗 Architecture

This project follows **Layered Architecture (N-Tier)** pattern:

```
┌─────────────────────────────────────┐
│      Controller Layer (REST API)    │
│         - AuthController            │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Service Layer (Business Logic) │
│      - UserService, JwtService      │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│   Repository Layer (Data Access)    │
│         - UserRepository            │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Entity Layer (Domain Models)   │
│            - User                   │
└─────────────────────────────────────┘
```

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 25** or higher
- **Maven 3.6+** (or use included Maven wrapper)
- **PostgreSQL 12+**
- **Git** (optional, for cloning)

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd luvojobs/demo
```

### 2. Database Setup

1. Create a PostgreSQL database named `luvojobs`
2. Update database credentials in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/luvojobs
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build the Project

```bash
# Using Maven wrapper (recommended)
./mvnw clean install

# Or using Maven directly
mvn clean install
```

### 4. Run the Application

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or using Maven directly
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## ⚙️ Configuration

### Application Properties

Key configuration in `src/main/resources/application.properties`:

```properties
# Application
spring.application.name=luvojobs

# Database
spring.datasource.url=jdbc:postgresql://host:port/database
spring.datasource.username=username
spring.datasource.password=password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=your-secret-key-here
jwt.accessTokenExpirationMs=86400000  # 24 hours
jwt.refreshTokenExpirationMs=604800000 # 7 days
```

## 📚 API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🔌 API Endpoints

### Authentication

#### Signup
```http
POST /api/auth/signup
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "username": "a1b2c3d4e5f6g7h8",
  "email": "user@example.com",
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "username": "a1b2c3d4e5f6g7h8",
  "email": "user@example.com",
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

## 📁 Project Structure

```
demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── config/          # Configuration classes
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/      # REST Controllers
│   │   │   │   └── AuthController.java
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── AuthResponse.java
│   │   │   │   ├── LoginRequest.java
│   │   │   │   └── SignupRequest.java
│   │   │   ├── entity/          # JPA Entities
│   │   │   │   └── User.java
│   │   │   ├── repository/      # Data Access Layer
│   │   │   │   └── UserRepository.java
│   │   │   ├── service/         # Business Logic Layer
│   │   │   │   ├── JwtService.java
│   │   │   │   └── UserService.java
│   │   │   └── LuvojobsApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                    # Test files
└── pom.xml
```

## 🔒 Security

### Password Security
- Passwords are hashed using **BCrypt** before storage
- Password validation (minimum 6 characters)

### JWT Tokens
- **Access Token**: Valid for 24 hours, contains user info
- **Refresh Token**: Valid for 7 days, used to refresh access tokens
- Tokens are signed using HMAC SHA algorithm

### Public Endpoints
The following endpoints are publicly accessible:
- `POST /api/auth/signup`
- `POST /api/auth/login`
- `GET /swagger-ui/**`
- `GET /v3/api-docs/**`

All other endpoints require authentication.

## 🗄 Database

### User Table Schema

The `users` table is automatically created with the following structure:

| Column      | Type          | Constraints           |
|-------------|---------------|-----------------------|
| id          | BIGINT        | PRIMARY KEY, AUTO_INCREMENT |
| email       | VARCHAR       | UNIQUE, NOT NULL      |
| password    | VARCHAR(60)   | NOT NULL              |
| username    | VARCHAR(16)   | UNIQUE, NOT NULL      |
| created_at  | TIMESTAMP     | NOT NULL              |
| updated_at  | TIMESTAMP     | NOT NULL              |

### Database Connection
The application uses **HikariCP** for connection pooling and automatically manages database schema updates via `hibernate.ddl-auto=update`.

## 🏃 Running the Application

### Using Maven Wrapper (Windows)
```bash
./mvnw.cmd spring-boot:run
```

### Using Maven Wrapper (Unix/Mac)
```bash
./mvnw spring-boot:run
```

### Using Maven Directly
```bash
mvn spring-boot:run
```

### Running Tests
```bash
./mvnw test
```

## 🧪 Testing

### Using Swagger UI

1. Start the application
2. Navigate to http://localhost:8080/swagger-ui.html
3. Use the interactive UI to test endpoints

### Using cURL

**Signup:**
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'
```

## 🔐 Environment Variables

For production, use environment variables instead of hardcoding values:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://host:port/database
export SPRING_DATASOURCE_USERNAME=username
export SPRING_DATASOURCE_PASSWORD=password
export JWT_SECRET=your-secret-key-here
```

Update `application.properties` to use environment variables:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
jwt.secret=${JWT_SECRET}
```

## 📝 Development Notes

- **Java Version**: Java 25
- **Spring Boot Version**: 4.0.1
- **Default Port**: 8080
- **Logging**: SQL queries are logged in console (can be disabled in production)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👤 Author

Luvojobs Team

## 📞 Support

For support, email support@luvojobs.com or open an issue in the repository.

---

**Note**: This is a demo project. Make sure to change default credentials and use environment variables for sensitive data in production.
