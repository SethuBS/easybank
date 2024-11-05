EasyBank Application
Overview
EasyBank is a simple banking application built with Spring Boot that simulates core banking operations, such as account withdrawals. The application is structured using Domain-Driven Design (DDD) principles, ensuring modularity, scalability, and ease of testing. It uses an in-memory H2 database for development and testing purposes, and integrates with AWS SNS for event notifications.

Features
RESTful API to handle account withdrawals
Real-time event notifications via AWS SNS
Transactional support for data consistency
Layered architecture following SOLID principles
Configurable with application.properties
Architecture
The application is structured into the following layers:

Presentation Layer: Handles HTTP requests (via BankAccountController).
Service Layer: Contains business logic (via BankAccountService).
Repository Layer: Manages data persistence (via BankAccountRepository).
Event Publishing Layer: Publishes events to SNS.
Domain Model Layer: Defines the core data models (BankAccount, WithdrawalEvent).
Technology Stack
Java 17
Spring Boot 3.x
Spring Data JPA
H2 In-Memory Database (for development/testing)
AWS SDK for Java (SNS)
JUnit 5 (for testing)
Getting Started
Prerequisites
Java 17 or higher
Maven 3.6 or higher
AWS account and IAM credentials (if using SNS for event publishing)
Setup and Configuration
Clone the repository:

bash
Copy code
git clone https://github.com/yourusername/easybank.git
cd easybank
Configure AWS SNS in src/main/resources/application.properties:

properties
Copy code
aws.sns.topicArn=arn:aws:sns:YOUR_REGION:YOUR_ACCOUNT_ID:YOUR_TOPIC_NAME
aws.region=YOUR_REGION
Build the project:

bash
Copy code
mvn clean install
Run the application:

bash
Copy code
mvn spring-boot:run
The application will start on http://localhost:8080.

API Endpoints
Withdraw Funds
URL: /bank/withdraw

Method: POST

Description: Withdraws funds from an account if the balance is sufficient.

Request Parameters:

accountId (Long): Account ID to withdraw from
amount (BigDecimal): Amount to withdraw
Example Request:

bash
Copy code
curl -X POST "http://localhost:8080/bank/withdraw?accountId=1&amount=100.00"
Responses:

200 OK: Withdrawal successful
400 Bad Request: Insufficient funds or other failure
500 Internal Server Error: Server error
Application Structure
plaintext
Copy code
easybank/
├── src/
│   ├── main/
│   │   ├── java/com/companyname/easybank/
│   │   │   ├── controller/         # REST controllers
│   │   │   ├── service/            # Business logic services
│   │   │   ├── repository/         # Data access layer
│   │   │   ├── event/              # Event publishing logic
│   │   │   ├── model/              # Domain models
│   │   │   └── config/             # Configuration classes
│   │   └── resources/
│   │       ├── application.properties # Configuration file
│   └── test/
│       └── java/com/companyname/easybank/ # Unit, integration, and end-to-end tests
└── README.md
Testing
This application includes the following types of tests:

Unit Tests: Tests individual components in isolation, using mocks.
Integration Tests: Tests interactions between components, especially around database operations.
End-to-End Tests: Tests complete workflows, from the controller through the repository.
Run all tests with:

bash
Copy code
mvn test
Deployment
Package the application:

bash
Copy code
mvn package
Deploy the JAR file to your desired environment, using the appropriate application.properties for production.

Configuration
You can configure the application in src/main/resources/application.properties.

For instance:

AWS SNS: Add the SNS Topic ARN and Region here for event publishing.
Database: By default, H2 in-memory is configured, but this can be swapped for a persistent database (like PostgreSQL or MySQL) for production.
Troubleshooting
AWS SNS Errors: Ensure you have valid AWS credentials set up and the Topic ARN is correct.
H2 Console Access: Access the H2 database console at http://localhost:8080/h2-console. Default JDBC URL: jdbc:h2:mem:testdb.
Future Enhancements
Additional Services: Implement additional services like deposit, account management, etc.
Microservices: Refactor into microservices for separate handling of features (e.g., withdrawals, notifications).
Distributed Tracing and Monitoring: Integrate tools like OpenTelemetry for observability.
Advanced Event Processing: Move to a more robust message broker if event traffic scales up (e.g., Apache Kafka).
License
This project is licensed under the MIT License - see the LICENSE file for details.
