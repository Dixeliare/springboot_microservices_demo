# Spring Boot Microservices - Car Rental Management System

A comprehensive microservices-based car rental management system built with Spring Boot, Spring Cloud, and PostgreSQL. This document explains how the system works, its architecture, and the flow of requests through the microservices.

## System Architecture

The system follows a microservices architecture pattern with the following components:

### Services Overview

1. **Eureka Server** (Port 8761)
   - Service Discovery Server
   - Central registry for all microservices
   - Provides health monitoring and service registration

2. **API Gateway** (Port 8000)
   - Single entry point for all client requests
   - JWT Authentication Filter
   - Request routing to appropriate microservices
   - Load balancing using service names from Eureka

3. **Authentication Service** (Port 8080)
   - User registration and login
   - JWT token generation and validation
   - User management with role-based access (ADMIN, CUSTOMER)
   - Database: IdentifyDB

4. **Car Management Service** (Port 8082)
   - CRUD operations for Cars
   - Manufacturer management
   - Supplier management
   - Car status management (AVAILABLE, RENTED, MAINTENANCE, SUSPENDED)
   - Database: CarDB

5. **Customer Service** (Port 8081)
   - Customer information management
   - Customer CRUD operations
   - Customer status management (ACTIVE, INACTIVE, SUSPENDED)
   - Database: CustomerDB

6. **Renting Service** (Port 8083)
   - Renting transaction management
   - Renting details management
   - Statistics and reporting
   - Inter-service communication via OpenFeign
   - Database: RentingDB

## How the System Works

### Service Discovery Pattern

All microservices register themselves with Eureka Server on startup. The registration process works as follows:

1. Each service (except Eureka Server) is configured as a Eureka Client
2. On startup, the service sends a registration request to Eureka Server
3. Eureka Server stores the service instance information (name, host, port, health status)
4. Services send heartbeats to Eureka every 30 seconds to maintain registration
5. If a service fails to send heartbeats, Eureka marks it as DOWN after 90 seconds

**Benefits:**
- Services can be discovered dynamically without hardcoding IP addresses
- Load balancing is handled automatically by Eureka
- Health monitoring is built-in

### Request Flow Through API Gateway

When a client makes a request, it follows this flow:

1. **Client Request** → API Gateway (Port 8000)
   - All external requests must go through the API Gateway
   - Gateway acts as a reverse proxy

2. **JWT Authentication Filter** (if not public endpoint)
   - Checks if the request path is public (/api/v1/auth/register, /api/v1/auth/login)
   - For protected endpoints, validates JWT token from Authorization header
   - Extracts username and role from token
   - Adds X-Username and X-Role headers to forward to downstream services
   - Returns 401 Unauthorized if token is invalid or missing

3. **Service Discovery and Routing**
   - Gateway uses Eureka to find the target service instance
   - Routes request based on path patterns:
     - /api/v1/auth/** → authentication-service
     - /api/v1/cars/**, /api/v1/manufacturers/**, /api/v1/suppliers/** → car-management-service
     - /api/v1/customers/** → customer-service
     - /api/v1/rentings/** → renting-service

4. **Load Balancing**
   - Gateway uses "lb://" prefix to enable load balancing
   - Eureka provides multiple instances if available
   - Requests are distributed across instances

5. **Response Flow**
   - Target service processes the request
   - Response is sent back through Gateway
   - Gateway forwards response to client

### JWT Authentication Flow

The authentication system uses JWT (JSON Web Tokens) for stateless authentication:

1. **User Registration**
   - POST /api/v1/auth/register
   - Client sends email, password, and role
   - AuthenticationService hashes password using BCrypt
   - User is saved to IdentifyDB
   - Returns success message

2. **User Login**
   - POST /api/v1/auth/login
   - Client sends email and password
   - AuthenticationService validates credentials
   - If valid, generates JWT token containing:
     - Subject: user email
     - Claims: roles, user ID
     - Expiration: 24 hours (86400000ms)
   - Returns JWT token in response

3. **Token Usage**
   - Client includes token in Authorization header: "Bearer <token>"
   - API Gateway validates token before forwarding request
   - Token is validated using shared secret key
   - Username and role are extracted and forwarded to services

4. **Token Validation**
   - Gateway's JwtUtil validates token signature
   - Checks token expiration
   - Extracts user information
   - Returns 401 if token is invalid or expired

### Inter-Service Communication with OpenFeign

RentingService communicates with other services using OpenFeign, a declarative HTTP client:

1. **Feign Client Interfaces**
   - CarServiceClient: Declares methods to call CarManagementService
   - CustomerServiceClient: Declares methods to call CustomerService
   - Uses @FeignClient annotation with service name from Eureka

2. **Service Discovery Integration**
   - Feign uses Eureka to resolve service names to actual instances
   - Automatically handles load balancing
   - Retries failed requests (configurable)

3. **Request Flow Example: Creating a Renting Transaction**
   - Client sends POST /api/v1/rentings to API Gateway
   - Gateway validates JWT and routes to RentingService
   - RentingService.createTransaction() is called
   - Service validates customer exists by calling CustomerServiceClient.getCustomerById()
   - Service validates each car exists and is available by calling CarServiceClient.getCarById()
   - If valid, transaction is created in RentingDB
   - Returns transaction details to client

4. **Automatic Car Status Updates**
   - When transaction status changes to APPROVED:
     - RentingService calls CarServiceClient.updateCarStatus() for each car
     - Car status changes from AVAILABLE to RENTED
   - When transaction is REJECTED or COMPLETED:
     - Car status changes back to AVAILABLE

### Database per Service Pattern

Each microservice has its own database, ensuring data isolation:

- **IdentifyDB**: Stores user authentication data (AuthenticationService)
- **CarDB**: Stores car, manufacturer, and supplier data (CarManagementService)
- **CustomerDB**: Stores customer information (CustomerService)
- **RentingDB**: Stores renting transactions and details (RentingService)

**Benefits:**
- Services are independent and can scale separately
- Database schema changes don't affect other services
- Each service can use the database technology best suited for its needs
- Better fault isolation

### Business Logic Flow

#### Creating a Renting Transaction

1. Client sends request with customerId and rentingDetails (carId, startDate, endDate, price)
2. RentingService validates:
   - Customer exists (calls CustomerService via Feign)
   - Each car exists and is AVAILABLE (calls CarManagementService via Feign)
   - Start date is before end date
   - Start date is not in the past
3. If price is not provided, calculates automatically:
   - Gets car's daily rate from CarManagementService
   - Calculates number of days between start and end date
   - Price = daily rate × number of days
4. Creates transaction with status PENDING
5. Calculates total price from all details
6. Saves to RentingDB
7. Returns transaction with generated ID

#### Updating Transaction Status

1. Client sends PUT request with new status (APPROVED, REJECTED, COMPLETED)
2. RentingService updates transaction status
3. If status is APPROVED:
   - Calls CarServiceClient.updateCarStatus() for each car
   - Changes car status to RENTED
4. If status is REJECTED or COMPLETED:
   - Changes car status back to AVAILABLE
5. Returns updated transaction

#### Statistics and Reporting

1. Client requests statistics with optional date range
2. RentingService queries RentingDB using custom repository methods
3. Calculates:
   - Total transactions in date range
   - Transactions by status (PENDING, APPROVED, COMPLETED, REJECTED)
   - Total revenue (sum of approved and completed transactions)
   - Average transaction value
4. Returns statistics DTO

## Prerequisites

- Java 21
- Maven 3.6+
- PostgreSQL 12+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## Setup Instructions

### 1. Database Setup

Create the following PostgreSQL databases:

```sql
CREATE DATABASE IdentifyDB;
CREATE DATABASE CarDB;
CREATE DATABASE CustomerDB;
CREATE DATABASE RentingDB;
```

Update database credentials in each service's application.properties:
- spring.datasource.username=postgres
- spring.datasource.password=your_password

### 2. Start Services (in order)

The order matters because services depend on Eureka Server:

1. **Eureka Server**
   ```bash
   cd EurekaServer
   mvn spring-boot:run
   ```
   - Verify: http://localhost:8761
   - Wait until Eureka Server is fully started

2. **Authentication Service**
   ```bash
   cd AuthenticationService
   mvn spring-boot:run
   ```
   - Registers with Eureka as "authentication-service"

3. **Car Management Service**
   ```bash
   cd CarManagementService
   mvn spring-boot:run
   ```
   - Registers with Eureka as "car-management-service"

4. **Customer Service**
   ```bash
   cd CustomerService
   mvn spring-boot:run
   ```
   - Registers with Eureka as "customer-service"

5. **Renting Service**
   ```bash
   cd RentingService
   mvn spring-boot:run
   ```
   - Registers with Eureka as "renting-service"
   - Requires CarManagementService and CustomerService to be running for Feign calls

6. **API Gateway**
   ```bash
   cd ApiGateway
   mvn spring-boot:run
   ```
   - Registers with Eureka as "api-gateway"
   - Requires all other services to be running for proper routing

### 3. Verify Services

Check Eureka Dashboard: http://localhost:8761

All services should appear in the "Instances currently registered with Eureka" section with status UP.

## Authentication

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

Response includes accessToken - use this token in subsequent requests.

### Using JWT Token

Add to request headers:
```
Authorization: Bearer <your-access-token>
```

Public endpoints (no token required):
- POST /api/v1/auth/register
- POST /api/v1/auth/login

All other endpoints require a valid JWT token.

## API Endpoints

### Authentication Service
- POST /api/v1/auth/register - Register new user
- POST /api/v1/auth/login - Login and get JWT token

### Car Management Service
- GET /api/v1/cars - Get all cars
- GET /api/v1/cars/{id} - Get car by ID
- GET /api/v1/cars/available - Get available cars
- POST /api/v1/cars - Create new car
- PUT /api/v1/cars/{id} - Update car
- PUT /api/v1/cars/{id}/status - Update car status
- DELETE /api/v1/cars/{id} - Delete car
- GET /api/v1/manufacturers - Get all manufacturers
- GET /api/v1/manufacturers/{id} - Get manufacturer by ID
- POST /api/v1/manufacturers - Create manufacturer
- PUT /api/v1/manufacturers/{id} - Update manufacturer
- DELETE /api/v1/manufacturers/{id} - Delete manufacturer
- GET /api/v1/suppliers - Get all suppliers
- GET /api/v1/suppliers/{id} - Get supplier by ID
- POST /api/v1/suppliers - Create supplier
- PUT /api/v1/suppliers/{id} - Update supplier
- DELETE /api/v1/suppliers/{id} - Delete supplier

### Customer Service
- GET /api/v1/customers - Get all customers
- GET /api/v1/customers/{id} - Get customer by ID
- GET /api/v1/customers/user/{userId} - Get customer by user ID
- POST /api/v1/customers - Create new customer
- PUT /api/v1/customers/{id} - Update customer
- DELETE /api/v1/customers/{id} - Delete customer

### Renting Service
- POST /api/v1/rentings - Create renting transaction
- GET /api/v1/rentings/{id} - Get transaction by ID
- GET /api/v1/rentings/customer/{customerId} - Get transactions by customer
- PUT /api/v1/rentings/{id}/status - Update transaction status
- DELETE /api/v1/rentings/{id} - Delete transaction
- GET /api/v1/rentings/statistics?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD - Get statistics

## Technology Stack

- Spring Boot 3.5.6
- Spring Cloud 2025.0.0
- Spring Cloud Gateway - API Gateway and routing
- Netflix Eureka - Service Discovery
- OpenFeign - HTTP Client for microservices communication
- PostgreSQL - Relational database
- JWT (jjwt 0.12.5) - Authentication and authorization
- Lombok - Boilerplate code reduction
- Maven - Build and dependency management

## Project Structure

```
SpingbootMicroservices/
├── EurekaServer/          - Service Discovery Server
├── ApiGateway/            - API Gateway with JWT Filter
├── AuthenticationService/  - User authentication and JWT generation
├── CarManagementService/   - Car, Manufacturer, Supplier management
├── CustomerService/        - Customer information management
├── RentingService/         - Renting transaction management
└── README.md
```

Each service follows standard Spring Boot structure:
- controllers/ - REST controllers handling HTTP requests
- services/ - Business logic layer
- repositories/ - Data access layer (JPA repositories)
- models/ - Entity models (JPA entities)
- dtos/ - Data Transfer Objects for API requests/responses
- config/ - Configuration classes
- exceptions/ - Exception handlers
- feign/ - Feign client interfaces (RentingService only)

## Testing

### Using Postman

1. Set up environment variables:
   - base_url: http://localhost:8000
   - token: JWT token from login response

2. Test flow:
   - Register/Login → Get token
   - Use token in Authorization header for subsequent requests
   - Test CRUD operations on each service

### Example: Creating a Renting Transaction

1. Ensure you have:
   - A customer in CustomerDB
   - A car with status AVAILABLE in CarDB

2. Create transaction:
```bash
POST http://localhost:8000/api/v1/rentings
Authorization: Bearer <token>
Content-Type: application/json

{
  "customerId": 1,
  "rentingDetails": [
    {
      "carId": 1,
      "startDate": "2025-12-01",
      "endDate": "2025-12-05"
    }
  ]
}
```

The system will:
- Validate customer exists
- Validate car exists and is available
- Validate dates (start < end, start not in past)
- Auto-calculate price if not provided
- Create transaction with status PENDING

## Statistics Endpoint

Get rental statistics for a date range:

```bash
GET http://localhost:8000/api/v1/rentings/statistics?startDate=2025-11-01&endDate=2025-11-30
Authorization: Bearer <token>
```

Response includes:
- Total transactions in date range
- Transactions by status (Approved, Completed, Rejected, Pending)
- Total revenue (from approved and completed transactions)
- Average transaction value

If startDate or endDate is not provided, defaults to last 30 days.

## Troubleshooting

### Service not appearing in Eureka
- Check if service is running and has no startup errors
- Verify Eureka server URL in application.properties matches actual Eureka server
- Check network connectivity between service and Eureka server
- Look for registration errors in service logs

### 503 Service Unavailable from Gateway
- Ensure target service is registered with Eureka (check Eureka dashboard)
- Check service name in API Gateway routes matches Eureka registration name
- Verify service is running and healthy
- Check if service port is correct

### JWT Token Invalid
- Token may have expired (default expiration: 24 hours)
- Re-login to get new token
- Verify token is included in Authorization header with "Bearer " prefix
- Ensure JWT secret key matches between AuthenticationService and ApiGateway

### Feign Client Errors
- Ensure target service is running and registered with Eureka
- Check service name in @FeignClient annotation matches Eureka registration
- Verify endpoint paths match actual controller paths
- Check network connectivity between services

### Database Connection Errors
- Verify PostgreSQL is running
- Check database name, username, and password in application.properties
- Ensure database exists
- Check PostgreSQL port (default: 5432)

## Configuration Notes

- All services use PostgreSQL with separate databases (Database per Service pattern)
- JWT secret key must be identical in AuthenticationService and ApiGateway application.properties
- Services automatically create database tables on startup (hibernate.ddl-auto=update)
- Default JWT expiration: 86400000ms (24 hours)
- Eureka heartbeat interval: 30 seconds
- Eureka lease expiration: 90 seconds

## Roles

- ADMIN: Full access to all endpoints (can manage cars, customers, transactions)
- CUSTOMER: Limited access (can manage own profile and create renting transactions)

## License

This is a demo project for educational purposes.

## Contributing

Feel free to fork and submit pull requests for improvements.
