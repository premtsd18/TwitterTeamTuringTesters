
# Twitter Clone Microservices Project

This repository contains the source code for a **Twitter Clone Application** developed using **Spring Boot** and **Microservices Architecture**. The project includes core features like user registration, authentication, posting tweets, following users, and managing roles and permissions.

## Features

1. **User Management**:
    - User Registration and Login (JWT Authentication).
    - Password Encryption (using BCrypt).
    - Role-based Access Control.

2. **Post Management**:
    - Create, Read, Update, and Delete (CRUD) operations for posts.
    - Streamlined and modular post services.

3. **Follow and Notifications**:
    - Follow/Unfollow users.
    - Notification system for user actions.

4. **Microservices Architecture**:
    - Modular services for User, Post, and Connections.
    - Inter-service communication using REST APIs.

5. **Best Practices**:
    - Follows **SOLID Principles** and incorporates **Design Patterns** like Strategy and Factory.
    - Centralized exception handling.

6. **Database**:
    - Relational Database integration (e.g., MySQL/PostgreSQL).
    - JPA/Hibernate for ORM.

## Technologies Used

- **Backend**: Spring Boot, Spring Data JPA, Hibernate.
- **Authentication**: JWT (JSON Web Token).
- **Database**: MySQL/PostgreSQL.
- **Dependency Management**: Maven.
- **Design Principles**: SOLID, Design Patterns.

## Setup Instructions

### Prerequisites
1. Install **Java 17+**.
2. Install **Maven**.
3. Install and configure **MySQL** or **PostgreSQL**.

### Steps to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repository/twitter-clone.git
   cd twitter-clone
   ```

2. Update the `application.properties` file with your database credentials.

3. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. Access the application:
    - API Base URL: `http://localhost:8080`
    - API Cloud URL: `http://twitter-team-turning-testers-19648cf420b7.herokuapp.com`

## Project Structure

```
src/
├── main/
│   ├── java/com/premtsd/twitter/teamturingtesters/
│   │   ├── config/         # Application Configuration Files
│   │   ├── controller/     # REST Controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA Entities
│   │   ├── repository/     # Spring Data JPA Repositories
│   │   ├── service/        # Business Logic Services
│   │   └── utils/          # Utility Classes
│   ├── resources/
│       ├── application.properties # Configuration File
│       └── static/                 # Static Files
└── test/                           # Unit and Integration Tests
```

## Future Enhancements
1. Add WebSocket support for real-time notifications.
2. Implement Docker and Kubernetes for containerization and orchestration.
3. Expand to a fully scalable microservices architecture.

## Contributing

Feel free to fork this repository and contribute via pull requests. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the [MIT License](LICENSE).

---

Developed by [Team Turing Testers]
