# Spring Boot Microservices - Car Rental Management System

A comprehensive microservices-based car rental management system built with Spring Boot, Spring Cloud, and PostgreSQL.

## 🏗️ Architecture

This project implements a microservices architecture with the following components:

### Services

1. **Eureka Server** (Port 8761)
   - Service Discovery Server
   - Monitors and manages all microservices

2. **API Gateway** (Port 8000)
   - Single entry point for all client requests
   - JWT Authentication Filter
   - Routes requests to appropriate microservices

3. **Authentication Service** (Port 8080)
   - User registration and login
   - JWT token generation and validation
   - Database: `IdentifyDB`

4. **Car Management Service** (Port 8082)
   - CRUD operations for Cars
   - Manufacturer management
   - Supplier management
   - Database: `CarDB`

5. **Customer Service** (Port 8081)
   - Customer information management
   - Customer CRUD operations
   - Database: `CustomerDB`

6. **Renting Service** (Port 8083)
   - Renting transaction management
   - Renting details management
   - Statistics and reporting
   - Inter-service communication via OpenFeign
   - Database: `RentingDB`

## 🚀 Prerequisites

- Java 21
- Maven 3.6+
- PostgreSQL 12+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## 📦 Setup Instructions

### 1. Database Setup

Create the following PostgreSQL databases:

```sql
CREATE DATABASE IdentifyDB;
CREATE DATABASE CarDB;
CREATE DATABASE CustomerDB;
CREATE DATABASE RentingDB;
```

Update database credentials in each service's `application.properties`:
- `spring.datasource.username=postgres`
- `spring.datasource.password=your_password`

### 2. Start Services (in order)

1. **Eureka Server**
   ```bash
   cd EurekaServer
   mvn spring-boot:run
   ```
   - Verify: http://localhost:8761

2. **Authentication Service**
   ```bash
   cd AuthenticationService
   mvn spring-boot:run
   ```

3. **Car Management Service**
   ```bash
   cd CarManagementService
   mvn spring-boot:run
   ```

4. **Customer Service**
   ```bash
   cd CustomerService
   mvn spring-boot:run
   ```

5. **Renting Service**
   ```bash
   cd RentingService
   mvn spring-boot:run
   ```

6. **API Gateway**
   ```bash
   cd ApiGateway
   mvn spring-boot:run
   ```

### 3. Verify Services

Check Eureka Dashboard: http://localhost:8761

All services should appear in the "Instances currently registered with Eureka" section.

## 🔐 Authentication

### Register a new user
```bash
POST http://localhost:8000/api/v1/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "role": "CUSTOMER"
}
```

### Login
```bash
POST http://localhost:8000/api/v1/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

Response includes `accessToken` - use this token in subsequent requests.

### Using JWT Token

Add to request headers:
```
Authorization: Bearer <your-access-token>
```

## 📡 API Endpoints

### Authentication Service
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - Login and get JWT token

### Car Management Service
- `GET /api/v1/cars` - Get all cars
- `GET /api/v1/cars/{id}` - Get car by ID
- `GET /api/v1/cars/available` - Get available cars
- `POST /api/v1/cars` - Create new car
- `PUT /api/v1/cars/{id}` - Update car
- `PUT /api/v1/cars/{id}/status` - Update car status
- `DELETE /api/v1/cars/{id}` - Delete car
- `GET /api/v1/manufacturers` - Get all manufacturers
- `GET /api/v1/suppliers` - Get all suppliers

### Customer Service
- `GET /api/v1/customers` - Get all customers
- `GET /api/v1/customers/{id}` - Get customer by ID
- `GET /api/v1/customers/user/{userId}` - Get customer by user ID
- `POST /api/v1/customers` - Create new customer
- `PUT /api/v1/customers/{id}` - Update customer
- `DELETE /api/v1/customers/{id}` - Delete customer

### Renting Service
- `POST /api/v1/rentings` - Create renting transaction
- `GET /api/v1/rentings/{id}` - Get transaction by ID
- `GET /api/v1/rentings/customer/{customerId}` - Get transactions by customer
- `PUT /api/v1/rentings/{id}/status` - Update transaction status
- `DELETE /api/v1/rentings/{id}` - Delete transaction
- `GET /api/v1/rentings/statistics?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD` - Get statistics

## 🔄 Inter-Service Communication

The system uses **OpenFeign** for inter-service communication:

- **RentingService** calls **CarManagementService** to:
  - Validate car availability
  - Update car status when transaction is approved/rejected

- **RentingService** calls **CustomerService** to:
  - Validate customer existence

## ✨ Features

### Business Logic
- **Date Validation**: Ensures start date is before end date and not in the past
- **Auto Price Calculation**: Automatically calculates rental price based on car's daily rate and rental period
- **Car Status Management**: Automatically updates car status when transactions are approved/rejected
- **Statistics**: Provides revenue and transaction statistics by date range

### Security
- JWT-based authentication
- Token validation at API Gateway level
- Role-based access control (ADMIN, CUSTOMER)

## 🛠️ Technology Stack

- **Spring Boot 3.5.6**
- **Spring Cloud 2025.0.0**
- **Spring Cloud Gateway** - API Gateway
- **Netflix Eureka** - Service Discovery
- **OpenFeign** - HTTP Client for microservices
- **PostgreSQL** - Database
- **JWT (jjwt 0.12.5)** - Authentication
- **Lombok** - Boilerplate code reduction
- **Maven** - Build tool

## 📁 Project Structure

```
SpingbootMicroservices/
├── EurekaServer/
├── ApiGateway/
├── AuthenticationService/
├── CarManagementService/
├── CustomerService/
├── RentingService/
└── README.md
```

Each service follows standard Spring Boot structure:
- `controllers/` - REST controllers
- `services/` - Business logic
- `repositories/` - Data access layer
- `models/` - Entity models
- `dtos/` - Data Transfer Objects
- `config/` - Configuration classes
- `exceptions/` - Exception handlers

## 🧪 Testing

### Using Postman

1. Import the API collection (if available)
2. Set up environment variables:
   - `base_url`: http://localhost:8000
   - `token`: JWT token from login response

3. Test flow:
   - Register/Login → Get token
   - Use token in Authorization header
   - Test CRUD operations

## 📊 Statistics Endpoint

Get rental statistics for a date range:

```bash
GET http://localhost:8000/api/v1/rentings/statistics?startDate=2025-11-01&endDate=2025-11-30
Authorization: Bearer <token>
```

Response includes:
- Total transactions
- Transactions by status (Approved, Completed, Rejected, Pending)
- Total revenue
- Average transaction value

## 🐛 Troubleshooting

### Service not appearing in Eureka
- Check if service is running
- Verify Eureka server URL in `application.properties`
- Check network connectivity

### 503 Service Unavailable
- Ensure target service is registered with Eureka
- Check service name matches in API Gateway routes
- Verify service is running and healthy

### JWT Token Invalid
- Token may have expired (default: 24 hours)
- Re-login to get new token
- Verify token is included in Authorization header

## 📝 Notes

- All services use PostgreSQL with separate databases (Database per Service pattern)
- JWT secret key must be the same across AuthenticationService and ApiGateway
- Services automatically create database tables on startup (hibernate.ddl-auto=update)
- Default JWT expiration: 86400000ms (24 hours)

## 👥 Roles

- **ADMIN**: Full access to all endpoints
- **CUSTOMER**: Limited access (can manage own profile and create renting transactions)

## 📄 License

This is a demo project for educational purposes.

## 🤝 Contributing

Feel free to fork and submit pull requests for improvements.

---

**Happy Coding! 🚀**
