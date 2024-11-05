# EasyBank Application

## Overview

EasyBank is a simple banking application built with **Spring Boot** that simulates core banking operations, such as account withdrawals. The application is structured using **Domain-Driven Design (DDD)** principles, ensuring modularity, scalability, and ease of testing. It uses an **in-memory H2 database** for development and testing purposes, and integrates with **AWS SNS** for event notifications.

## Features

- RESTful API to handle account withdrawals
- Real-time event notifications via AWS SNS
- Transactional support for data consistency
- Layered architecture following SOLID principles
- Configurable with `application.properties`

## Architecture

The application is structured into the following layers:

1. **Presentation Layer**: Handles HTTP requests (via `BankAccountController`).
2. **Service Layer**: Contains business logic (via `BankAccountService`).
3. **Repository Layer**: Manages data persistence (via `BankAccountRepository`).
4. **Event Publishing Layer**: Publishes events to SNS.
5. **Domain Model Layer**: Defines the core data models (`BankAccount`, `WithdrawalEvent`).

## Technology Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **H2 In-Memory Database** (for development/testing)
- **AWS SDK for Java (SNS)**
- **JUnit 5** (for testing)

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- AWS account and IAM credentials (if using SNS for event publishing)

### Setup and Configuration

1. Clone the repository:

   ```bash
   git clone https://github.com/SethuBS/easybank.git
   cd easybank
