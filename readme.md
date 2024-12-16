# Twitter Clone Microservices Project

This repository contains the source code for a **Twitter Clone Application** developed using **Spring Boot** and **Microservices Architecture**. The project includes core features like user registration, authentication, posting tweets, following users, and managing roles and permissions.

## Features

1. **User Management**:
   - User Registration and Login (JWT Authentication).
   - Password Encryption (using BCrypt).
   - Fetch all users.

2. **Post Management**:
   - Fetch posts by ID or user.
   - Fetch notifications for users.

3. **Follow and Connections**:
   - Follow/Unfollow users.
   - Fetch posts from user connections.

4. **Technologies Used**:
   - **Backend**: Spring Boot, Spring Data JPA, Hibernate.
   - **Authentication**: JWT (JSON Web Token).
   - **Database**: PostgreSQL.
   - **Message Broker**: Apache Kafka (Confluent Cloud).
   - **Dependency Management**: Maven.

## API Endpoints

### **UserController**
| HTTP Method | Endpoint               | Description                |
|-------------|------------------------|----------------------------|
| `POST`      | `/signup`              | Register a new user        |
| `POST`      | `/login`               | User login (authentication)|
| `GET`       | `/getAllUsers`         | Fetch all users            |

### **PostsController**
| HTTP Method | Endpoint                     | Description                             |
|-------------|------------------------------|-----------------------------------------|
| `GET`       | `/posts`                     | Fetch all posts                         |
| `GET`       | `/{postId}`                  | Fetch post details by ID                |
| `GET`       | `/users/{userId}/allPosts`   | Fetch all posts by a specific user      |
| `GET`       | `/users/{userId}/allNotifications` | Fetch notifications for a specific user |

### **ConnectionController**
| HTTP Method | Endpoint         | Description                      |
|-------------|------------------|----------------------------------|
| `POST`      | `/follow`        | Follow a user                    |
| `GET`       | `/getAllPosts`   | Fetch posts in user connections  |
| `REQUEST`   | `/connection`    | Connection-related operations    |

## Configuration Setup

Ensure your `application.properties` file is properly configured with the following details:

```properties
# Application Name
spring.application.name=twitter

# Server Configuration
server.port=7799

# JWT Configuration
jwt.secretKey=encrypted

# Database Configuration (PostgreSQL)
spring.jpa.hibernate.ddl-auto=validate
spring.datasource.url=jdbc:postgresql://autorack.proxy.rlwy.net:57712/linkedIn
spring.datasource.username=postgres
spring.datasource.password=encrypted

# Logging Configuration
logging.level.org.springframework.security.*=TRACE
logging.level.org.springframework.web.*=TRACE

# Kafka Configuration (Confluent Cloud)
spring.kafka.properties.sasl.mechanism=PLAIN
spring.kafka.bootstrap-servers=pkc-z9doz.eu-west-1.aws.confluent.cloud:9092
spring.kafka.properties.sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username='WABXE4BB73DP2XRC' password='hQZuLdgtmp9dnSBqrEv3eM5YLXN1zQXKW3bzTugbsK9EgBn8nQOGMLUjlta4P6lD';
spring.kafka.properties.security.protocol=SASL_SSL
spring.kafka.properties.session.timeout.ms=45000

# Kafka Client ID
client.id=ccloud-springboot-client-d307d509-09c7-4e42-95b9-b9ac281a080c
```

## Setup Instructions

### Prerequisites
1. Install **Java 17+**.
2. Install **Maven**.
3. Install and configure **PostgreSQL**.
4. Set up **Confluent Cloud Kafka** with the provided credentials.

### Steps to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repository/twitter-clone.git
   cd twitter-clone
   ```

2. Update the `application.properties` file with your database and Kafka credentials.

3. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. Access the application:
   - API Base URL: `http://localhost:7799`
   - API Cloud URL: `https://twitter-team-turning-testers-19648cf420b7.herokuapp.com`

## Future Enhancements
1. Add WebSocket support for real-time notifications.
2. Implement Docker and Kubernetes for containerization and orchestration.
3. Expand to a fully scalable microservices architecture.

---

Developed by [Team Turing Testers](https://github.com/premtsd-code)
