# Bank Account Management System

## Project Summary

This project is a Spring Boot MVC web application for creating and managing bank accounts. Users can submit account information through a web form, save checking, savings, and IRA accounts, review saved accounts in a separate listing page, and manage named deposit and withdrawal transactions through an account history screen. Transactions can be rolled back to apply the opposite balance change and remove the transaction record. The application uses Thymeleaf for the user interface and SQLite with JDBC for persistent data storage.

## Project Tasks

- Build a Spring Boot MVC application structure
- Create a landing page, account form, results page, and saved accounts page
- Implement validation for submitted account data
- Support multiple account types with object-oriented design
- Store and retrieve account records from an SQLite database
- Display saved account details in a formatted table
- Record named deposit and withdrawal transactions with rollback support

## Project Skills Learned

- Java and Spring Boot application development
- MVC architecture with controllers, services, and views
- Thymeleaf template rendering
- Form validation with Jakarta Validation
- JDBC database access with SQLite
- Object-oriented programming using abstraction, inheritance, polymorphism, and composition
- Transaction logging and balance rollback workflows

## Languages and Tools Used

- Java 17
- Spring Boot 3
- Thymeleaf
- SQLite
- JDBC
- Maven
- HTML and CSS

## Development Process Used

- Incremental development with separate model, controller, service, and storage layers
- Object-oriented design to represent different account types and shared account behavior

## Running the Project

1. Make sure the VS Code Java extension is configured with Java 17 and Maven.
2. Click run from BankAccountManagementApplication.java.
3. Open `http://localhost:8080/home` in your browser.

The SQLite database file is created automatically at `data/bank.db`.
