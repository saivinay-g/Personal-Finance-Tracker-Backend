# Personal Finance Tracker

A Spring Boot REST API for tracking personal expenses per user. It supports basic user registration/login and CRUD operations for expenses, with global exception handling and service-layer logging.

## Tech Stack
- Java 21
- Spring Boot (Web MVC, Security)
- Spring Data JPA (Hibernate)
- MySQL
- Lombok
- JUnit + Mockito (tests)

## Features
- User management
    - Register a new user
    - Login (API-level login endpoint)
    - Fetch a user by id
    - List all users
- Expense management
    - Create an expense for a user
    - List expenses (all / by user)
    - Fetch a specific expense by userId + expenseId
    - Update an expense by id
    - Delete an expense by id
- Centralized error responses (`GlobalExceptionHandler`)
- Service method logging via AOP (`LoggingAspect`)
- Basic authentication via Spring Security (HTTP Basic)

## Project Structure
- `src/main/java` — application source
- `src/main/resources/application.properties` — app + DB configuration
- `src/test/java` — unit tests (Mockito)
- `logs/` — application log output (configured in `application.properties`)

## Prerequisites
- Java 21+
- Maven 3.9+
- MySQL running locally (or update configuration for your environment)

## Database Setup
Create a MySQL database:
CREATE DATABASE finance_tracker;


Update credentials as needed in:

`src/main/resources/application.properties`


> The schema is managed by Hibernate with `spring.jpa.hibernate.ddl-auto=update`.

## Running the Application

### Option A: Maven

bash mvn spring-boot:run


### Option B: Maven Wrapper
bash ./mvnw spring-boot:run


The API will be available at:
- `http://localhost:8080`

## Authentication (Spring Security)
All endpoints require authentication (HTTP Basic).

Example credentials are configured in the security config:
- `admin / admin123`
- `saivinay / saivinay123`

### Example (curl)
bash curl -u admin:admin123 [http://localhost:8080/api/v1/users/all](http://localhost:8080/api/v1/users/all)

## API Endpoints

Base paths:
- Auth: `/api/v1/auth`
- Users: `/api/v1/users`
- Expenses: `/api/v1/expense`

### Auth
#### Register
`POST /api/v1/auth/register`

Body:
json { "username": "john", "email": "john@example.com", "password": "secret" }

#### CSRF Token (if needed by a frontend)
`GET /api/v1/auth/get-token`

#### Session Id
`GET /api/v1/auth/get-sessionId`

### Users
#### Get user by id
`GET /api/v1/users/id/{id}`

#### Get all users
`GET /api/v1/users/all`

### Expenses
#### Get all expenses
`GET /api/v1/expense/all`

#### Get all expenses by user id
`GET /api/v1/expense/expenses/{user_id}`

#### Create an expense
`POST /api/v1/expense/expenses`

Body:
json { "userId": 1, "amount": 25.5, "category": "Food", "description": "Lunch" }


#### Get an expense by userId and expenseId
`GET /api/v1/expense/expenses/{userId}/{expenseId}`

#### Update an expense by expenseId
`PUT /api/v1/expense/expenses/{expenseId}`

Body:
json { "userId": 1, "amount": 30.0, "category": "Food", "description": "Lunch (updated)" 


#### Delete an expense by expenseId
`DELETE /api/v1/expense/expenses/{expenseId}`

## Error Handling
Errors are returned in a consistent JSON structure including:
- timestamp
- status
- error
- message
- path

Common exceptions:
- `ResourceNotFoundException` → 404
- `InvalidRequestException` → 400
- Generic exceptions → 500

## Logging
Logging is written to:
- `logs/application.log`

Configured in `application.properties`.

## Testing
Run unit tests with:
bash mvn test


## Notes / Known Limitations
- HTTP Basic users are configured in-memory (not persisted in DB).
- If you plan to use the `/auth/login` endpoint as a real authentication mechanism, you may want to revisit credential verification and switch to a token-based approach (e.g., JWT) for production use.

## License
Add a license (MIT/Apache-2.0/etc.) if you plan to publish this project.



