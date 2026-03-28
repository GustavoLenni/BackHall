
# Back-Hall - Financial Management API

A Spring Boot RESTful API for managing user accounts and personal finances. This application provides user authentication with BCrypt password encryption, user management, and comprehensive finance tracking capabilities.

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Docker Setup](#docker-setup)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Usage Examples](#usage-examples)
- [Security](#security)
- [Testing](#testing)
- [Contributing](#contributing)

## ✨ Features

- **User Management**: Create, read, update, and delete user accounts
- **Authentication**: Secure login with JWT (JSON Web Token) support
- **Password Encryption**: BCrypt password hashing for secure storage
- **Finance Tracking**: Create, read, update, and delete finance records
- **User-Specific Finance Records**: Each user can manage their own finance data
- **Type Classification**: Finance records include type classification (e.g., income, expense)
- **RESTful API**: Clean and intuitive REST endpoints
- **Spring Security**: Built-in security configuration
- **Docker Support**: Easy containerization with Docker and Docker Compose

## 🛠️ Technology Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring Security**
- **BCrypt** - Password encryption library
- **PostgreSQL 12+**
- **Docker & Docker Compose**
- **Maven**
- **JWT (JSON Web Tokens)**

## 📁 Project Structure

```
src/main/java/com/example/Back_Hall/
├── Application.java              # Spring Boot entry point
├── WebConfig.java                # Web configuration
├── config/
│   └── SecurityConfig.java       # Spring Security configuration
├── controllers/
│   ├── UserController.java       # User management endpoints
│   └── FinanceController.java    # Finance management endpoints
├── dtos/
│   ├── LoginDto.java             # Login request DTO
│   ├── UserDto.java              # User data transfer object
│   ├── UserResponseDto.java      # User response DTO
│   └── FinanceDto.java           # Finance data transfer object
├── model/
│   ├── User.java                 # User entity
│   ├── Finance.java              # Finance entity
│   └── enums/
│       └── Type.java             # Finance type enumeration
├── repositories/
│   ├── UserRepository.java       # User data access layer
│   └── FinanceRepository.java    # Finance data access layer
├── security/
│   └── JwtUtil.java              # JWT utility functions
└── Service/
    ├── UserService.java          # User business logic
    └── FinanceService.java       # Finance business logic

src/main/resources/
└── application.properties        # Application configuration
```

## 📦 Prerequisites

### Local Development
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12 or higher
- Git

### Docker Development
- Docker 20.10+
- Docker Compose 1.29+
- Git

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/Back-Hall.git
cd Back-Hall
```

### 2. Local Database Setup (Without Docker)

Create a PostgreSQL database:

```sql
CREATE DATABASE polls;
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application (Local)

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080` by default.

## 🐳 Docker Setup

### Using Docker Compose (Recommended)

Create a `docker-compose.yml` file in the project root:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    container_name: polls_db
    environment:
      POSTGRES_DB: polls
      POSTGRES_USER: docker
      POSTGRES_PASSWORD: docker
    ports:
      - "5433:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - back-hall-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U docker"]
      interval: 10s
      timeout: 5s
      retries: 5

  back-hall:
    build: .
    container_name: back_hall_api
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/polls
      SPRING_DATASOURCE_USERNAME: docker
      SPRING_DATASOURCE_PASSWORD: docker
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy
    networks:
      - back-hall-network

volumes:
  postgres_data:

networks:
  back-hall-network:
    driver: bridge
```

Create a `Dockerfile` in the project root:

```dockerfile
# Build stage
FROM maven:3.9-eclipse-temurin-17 as builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Running with Docker Compose

```bash
# Build and start services
docker-compose up --build

# Run in background
docker-compose up -d --build

# View logs
docker-compose logs -f back-hall

# Stop services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

### Manual Docker Build

```bash
# Build the Docker image
docker build -t back-hall:latest .

# Run the container with PostgreSQL
docker run -d \
  --name polls_db \
  -e POSTGRES_DB=polls \
  -e POSTGRES_USER=docker \
  -e POSTGRES_PASSWORD=docker \
  -p 5433:5432 \
  postgres:15-alpine

# Run the Back-Hall application
docker run -d \
  --name back_hall_api \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/polls \
  -e SPRING_DATASOURCE_USERNAME=docker \
  -e SPRING_DATASOURCE_PASSWORD=docker \
  -p 8080:8080 \
  --link polls_db:postgres \
  back-hall:latest
```

## ⚙️ Configuration

### application.properties

Update `src/main/resources/application.properties` with your database credentials:

```properties
spring.application.name=Initial
spring.datasource.url=jdbc:postgresql://localhost:5433/polls
spring.datasource.username=docker
spring.datasource.password=docker
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

**Configuration Parameters:**
- `spring.datasource.url`: PostgreSQL connection URL (adjust port and database name as needed)
- `spring.datasource.username`: Database username
- `spring.datasource.password`: Database password
- `spring.jpa.hibernate.ddl-auto=update`: Automatically update database schema

### Environment Variables (Docker)

When using Docker, you can override properties with environment variables:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/polls
SPRING_DATASOURCE_USERNAME=docker
SPRING_DATASOURCE_PASSWORD=docker
```

## 🔒 Security

### Password Encryption with BCrypt

This application uses **BCrypt** for secure password hashing:

- Passwords are automatically encrypted using BCrypt before storage in the database
- BCrypt provides adaptive hashing with salt to prevent rainbow table attacks
- It's automatically integrated with Spring Security

**Example Flow:**
1. User submits password during registration or login
2. Password is hashed using BCrypt with a randomly generated salt
3. Only the hashed password is stored in the database
4. During login, the submitted password is hashed and compared with the stored hash

**Dependencies in pom.xml:**
```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>
</dependency>
```

### Security Features

- **JWT Authentication**: Finance endpoints are protected with JWT tokens
- **Spring Security**: Configured security context for protected resources
- **BCrypt Hashing**: Industry-standard password encryption
- **CORS Configuration**: Configure WebConfig.java for cross-origin requests if needed

### JWT Token Structure

The JWT token is extracted from the Authorization header and used to identify the current user for finance records.

```
Authorization: Bearer <JWT_TOKEN>
```

## 📡 API Endpoints

### User Management

#### Get All Users
```
GET /users
```
Returns a list of all users.

**Response:**
```json
[
  {
    "id": 1,
    "email": "user@example.com"
  }
]
```

#### Create User
```
POST /users
```

**Request Body:**
```json
{
  "email": "newuser@example.com",
  "password": "secure_password"
}
```

**Response:** `201 Created`

#### Login
```
POST /users/login
```

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password"
}
```

**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs..."
}
```

#### Update User
```
PUT /users/{id}
```

**Request Body:**
```json
{
  "email": "updated@example.com",
  "password": "new_password"
}
```

#### Delete User
```
DELETE /users/{id}
```

**Response:** `200 OK` with deletion confirmation message.

### Finance Management

All finance endpoints require JWT authentication via the `Authorization` header.

#### Get User's Finances
```
GET /finances
```

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Response:**
```json
[
  {
    "id": 1,
    "product": "Grocery",
    "value": 50.00,
    "amount": 1,
    "type": "EXPENSE"
  }
]
```

#### Create Finance Record
```
POST /finances
```

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Request Body:**
```json
{
  "product": "Salary",
  "value": 5000.00,
  "amount": 1,
  "type": "INCOME"
}
```

**Response:** `201 Created`

#### Update Finance Record
```
PUT /finances/{id}
```

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Request Body:**
```json
{
  "product": "Updated Product",
  "value": 75.50,
  "amount": 2,
  "type": "INCOME"
}
```

#### Delete Finance Record
```
DELETE /finances/{id}
```

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Response:** `200 OK` with deletion confirmation

#### Delete All User's Finances
```
DELETE /finances/all
```

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Response:** `200 OK` with deletion confirmation

## 💡 Usage Examples

### Example 1: Create a User and Login (Local)

```bash
# Create user
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"email":"john@example.com","password":"password123"}'

# Login
curl -X POST http://localhost:8080/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@example.com","password":"password123"}'
```

### Example 2: Add Finance Records

```bash
# Set token variable
TOKEN="your_jwt_token_here"

# Add income
curl -X POST http://localhost:8080/finances \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"product":"Salary","value":5000,"amount":1,"type":"INCOME"}'

# Add expense
curl -X POST http://localhost:8080/finances \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"product":"Rent","value":1200,"amount":1,"type":"EXPENSE"}'
```

### Example 3: View and Update Finances

```bash
# Get all finances
curl -X GET http://localhost:8080/finances \
  -H "Authorization: Bearer $TOKEN"

# Update a finance record
curl -X PUT http://localhost:8080/finances/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"product":"Updated Expense","value":1500,"amount":1,"type":"EXPENSE"}'
```

## 🧪 Testing

Unit tests are located in `src/test/java/com/example/Back_Hall/`

Run tests with:

```bash
mvn test
```

Run tests in Docker:

```bash
docker-compose exec back-hall mvn test
```

## 📝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👤 Author

Developed as a Back-Hall financial management application.

## 🤝 Support

For support or questions, please create an issue in the repository.

---

**Last Updated:** March 2026
