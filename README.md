# Car Sharing Service

## Table of Contents
- [About the Project](#about-the-project)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [Testing](#testing)
- [API Overview](#api-overview)
  - [Detailed API Explanation](#detailed-api-explanation)
- [Contact](#contact)

---

## About the Project

Car Sharing Service is a backend application for a modern car-sharing platform. It allows users to register, book cars, manage rentals, handle payments, and receive notifications. The project is built with Java and Spring Boot, following a modular and scalable architecture.

---

## Features

- **User Management:** Registration, authentication, role management, and profile updates.
- **Car Management:** Add, update, delete, and filter cars by type, price, features, and availability.
- **Rental Management:** Book a car, return a car, calculate overdue fees, and view rental history.
- **Payment Integration:** Create and track payments for rentals.
- **Notifications:** Save notifications in the database and send emails for key events (booking, overdue, etc.).
- **Advanced Filtering:** Search cars by multiple criteria and date range.
- **Admin Dashboard:** Manage cars, users, and view analytics.

---

## Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring Data JPA (Hibernate)**
- **MySQL** (or compatible RDBMS)
- **Spring Security (JWT-based)**
- **MapStruct** (DTO mapping)
- **JUnit 5, Mockito** (testing)
- **Docker** (for containerization)

---

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven
- MySQL
- Docker (optional, for containerization)

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/anastasiia-peleshok/car-sharing.git
   cd car-sharing
   ```

2. **Configure the database:**
   - Create a MySQL database (e.g., `car_sharing_db`).
   - Update `src/main/resources/application.properties` with your DB credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/car_sharing_db
     spring.datasource.username=your_db_user
     spring.datasource.password=your_db_password
     ```

3. **Build the project:**
   ```bash
   ./mvnw clean install
   ```

### Running the Application

- **With Maven:**
  ```bash
  ./mvnw spring-boot:run
  ```
  The app will start on [http://localhost:8080](http://localhost:8080)

- **With Docker:**
  1. Build the Docker image:
     ```bash
     docker build -t car-sharing .
     ```
  2. Run the container:
     ```bash
     docker run -p 8080:8080 car-sharing
     ```

---

## Testing

Run all tests with:
```bash
./mvnw test
```

---

## API Overview

The API provides endpoints for:
- User registration, login, and management
- Car CRUD and filtering
- Rental creation, return, and history
- Payment creation and status tracking
- Notification management

API documentation is available via Swagger UI (if enabled in your project) at:
```
http://localhost:8080/swagger-ui.html
```

---

## Detailed API Explanation

### **User API** (`/user`, `/auth`)
- **Registration & Login:**
  - `POST /auth/registration` — Register a new user
  - `POST /auth/login` — Authenticate and receive JWT token
- **Profile Management:**
  - `GET /user/{id}` — Get user by ID
  - `GET /user/email/{email}` — Get user by email
  - `PUT /user/{id}` — Update user profile (manager only)
  - `PUT /user/role/{id}` — Change user role (manager only)
  - `DELETE /user/{id}` — Delete user
- **Responsibilities:** UserController, AuthenticationController, UserService

### **Car API** (`/car`)
- **Car Management:**
  - `GET /car` — List all cars
  - `GET /car/{carId}` — Get car by ID
  - `POST /car` — Add a new car (manager only)
  - `PUT /car/{carId}` — Update car (manager only)
  - `DELETE /car/{carId}` — Delete car (manager only)
- **Car Features:**
  - `POST /car/{carId}/{featureId}` — Add feature to car (manager only)
  - `GET /car/{carId}/features` — Get car with features
- **Filtering:**
  - `GET /car/filter` — Get cars by filter (send filters in request body)
  - `GET /car/available` — List available cars
  - `POST /car/available` — List available cars by date range and filters
- **Responsibilities:** CarController, CarService

### **Rental API** (`/rental`)
- **Rental Management:**
  - `POST /rental` — Create a new rental (book a car)
  - `PUT /rental/complete/{rentalId}` — Complete (return) a rental
  - `GET /rental/user/{userId}` — Get all rentals for a user
  - `GET /rental/{id}` — Get rental by ID
  - `GET /rental/overdue` — List overdue rentals
  - `GET /rental/amount/{rentalId}` — Get amount to pay for a rental
- **Responsibilities:** RentalController, RentalService

### **Payment API** (`/payment`)
- **Payment Management:**
  - `POST /payment` — Create a payment for a rental
  - `GET /payment` — List all payments
  - `GET /payment/{id}` — Get payment by ID
  - `GET /payment/status/{status}` — List payments by status
- **Responsibilities:** PaymentController, PaymentService

### **Features API** (`/feature`)
- **Features Management:**
  - `POST /feature` — Create a feature for a car
  - `GET /feature` — List all features
  - `GET /feature/{id}` — Get feature by ID
  - `PUT /feature/{id}` — Update feature by id
  - `DELETE /feature/{id}` — Delete feature by id
- **Responsibilities:** FeatureController, FeatureService
---

