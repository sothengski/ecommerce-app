# eCommerce-app Backend - Spring Boot

## Overview
This is the backend for an eCommerce application built with **Spring Boot**. The system provides user authentication, role management, product management, cart functionality, and order processing features. It is designed to be scalable, modular, and easily extendable.

## Features
- **User Management**: Login, Register, and Role-based authentication.
- **Role Management**: Admin and User roles.
- **Product Management**: Add, update, delete, and list products.
- **Cart Management**: Users can add, remove, and view items in their cart.
- **Category Management**: Admin can create, update, and delete categories.
- **Order Management**: Users can place orders and view order history.

## Tech Stack
- **Java 17+**
- **Spring Boot 2.x**
- **JPA/Hibernate** (for Database interactions)
- **Maven** (build automation)

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.x
- Postman (for API testing)

### Setup
1. Clone the repository:
    ```bash
    git clone https://github.com/sothengski/ecommerce-app.git
    cd ecommerce-app
    ```

2. Configure the `application.properties` for your database:
    ```properties
    spring.application.name=ecommerce-app
    # Config H2 database
    server.port=8080

    spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    # Enable H2 console
    spring.h2.console.enabled=true
    spring.h2.console.path=/h2-console
    # Hibernate settings to automatically create tables based on entities
    spring.jpa.hibernate.ddl-auto=create-drop

    # Reduce logging level. Set logging level to warn
    #logging.level.root=warn
    logging.level.org.springframework.web=DEBUG
    logging.level.org.hibernate.SQL=INFO
    logging.file=D:/log/myapp.log
    spring.http.log-request-details=true
    ```

3. Install dependencies and build the project:
    ```bash
    mvn clean install
    ```

4. Run the application:
    ```bash
    mvn spring-boot:run
    ```

5. The API will be available at `http://localhost:8080/api`.

6. Postman Collection:
    ```bash
    https://www.postman.com/sotheng-ski/sotheng/collection/onk338u/ecommerce-app-se?action=share&creator=10752563
    ```

---

## API Endpoints

### Authentication

#### POST /users
Registers a new user.

**Request Body**:
```json
{
    "email": "john@example.com",
    "password": "123123",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "6041231234",
    "address": "Vancouver",
    "active": false,
    "roleId": 2
}
```

**Response**:
```json
{
    "success": true,
    "message": "User created successfully",
    "data": {
        "id": 4,
        "email": "john@example.com",
        "firstName": "John",
        "lastName": "Doe",
        "phone": "6041231234",
        "address": "Vancouver",
        "active": false,
        "role": {
            "id": 2,
            "name": "seller"
        }
    },
    "error": null
}
```

---

### Notes:

- Customize the "License" section if you're using a different open-source license.
- The `application.properties` part assumes a MySQL database, but you can adjust it to suit PostgreSQL or another DB system as needed. 
