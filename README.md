# Messaging System

A modern, scalable messaging application built with Spring Boot, featuring user management, real-time messaging, and cloud integration capabilities.

## 🚀 Features

- **User Management**: Complete user registration, authentication, and profile management
- **Real-time Messaging**: Send and receive messages with support for different message types
- **Friend System**: Add friends, send invitations, and manage friend relationships
- **Cloud Integration**: AWS services integration for scalable messaging infrastructure
- **RESTful API**: Well-designed REST endpoints for frontend integration
- **Database Support**: MySQL for production, H2 for testing
- **Security**: JWT-based authentication and secure password handling

## 🏗️ Architecture

### Technology Stack
- **Backend**: Spring Boot 3.x, Java 21
- **Database**: MySQL (production), H2 (testing)
- **ORM**: MyBatis for database operations
- **Cloud**: AWS (CloudWatch, S3, DynamoDB, SQS)
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito, Spring Boot Test

### Project Structure
```
src/
├── main/
│   ├── java/com/songhanwu/messaging/
│   │   ├── controller/     # REST API endpoints
│   │   ├── service/        # Business logic
│   │   ├── dao/           # Data access objects
│   │   ├── dto/           # Data transfer objects
│   │   ├── configuration/ # Spring configuration
│   │   └── aspect/        # AOP aspects
│   └── resources/
│       ├── application.properties
│       └── init.sql       # Database schema
└── test/
    ├── java/              # Unit and integration tests
    └── resources/
        ├── application-test.properties
        └── schema.sql     # Test database schema
```

## 🛠️ Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+
- MySQL 8.0+ (for production)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/SonghanWu/messaging_songhan.git
   cd messaging_songhan
   ```

2. **Configure database**
   ```bash
   # Create MySQL database
   mysql -u root -p
   CREATE DATABASE messaging;
   ```

3. **Set up environment variables**
   ```bash
   # Copy and configure environment file
   cp .env.example .env
   # Edit .env with your database credentials and AWS settings
   ```

4. **Build and run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

### Docker Setup (Alternative)

```bash
# Start with Docker Compose
docker-compose up -d
```

## 📚 API Documentation

### User Management
- `POST /users/register` - Register new user
- `POST /users/login` - User login
- `GET /users/profile` - Get user profile
- `PUT /users/profile` - Update user profile

### Messaging
- `POST /messages/send` - Send message
- `GET /messages/received` - Get received messages
- `GET /messages/sent` - Get sent messages

### Friend Management
- `POST /friends/invite` - Send friend invitation
- `PUT /friends/invite/{id}/accept` - Accept invitation
- `PUT /friends/invite/{id}/decline` - Decline invitation
- `GET /friends` - Get friends list

## 🧪 Testing

### Run Tests
```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

### Test Configuration
- Tests use H2 in-memory database
- AWS services are automatically disabled in test environment
- Comprehensive unit and integration test coverage

## ☁️ Cloud Integration

### AWS Services
- **CloudWatch**: Application monitoring and logging
- **S3**: File storage for attachments
- **DynamoDB**: Message metadata storage
- **SQS**: Message queuing for scalability

### Configuration
AWS services can be enabled/disabled via configuration:
```properties
aws.enabled=true
aws.region=us-east-1
```

## 🚀 Deployment

### Production Deployment
1. Configure production database
2. Set up AWS credentials and services
3. Configure environment-specific properties
4. Build and deploy with your preferred method

### Environment Profiles
- `dev`: Development environment
- `test`: Testing environment
- `prod`: Production environment

## 📈 Performance & Monitoring

- Application metrics via Spring Boot Actuator
- CloudWatch integration for monitoring
- Comprehensive logging with structured format
- Database connection pooling for optimal performance

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Songhan Wu**
- GitHub: [@SonghanWu](https://github.com/SonghanWu)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- AWS for cloud services integration
- Open source community for various libraries and tools
