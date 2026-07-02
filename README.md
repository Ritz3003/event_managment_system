# 🎉 Event Platform

Event Management Platform built with **Spring Boot** that allows organizers to create and manage events while attendees can register, receive QR-code tickets, check in, and receive email notifications.

## 🚀 Features

### 🔐 Authentication & Security

* JWT Authentication
* Spring Security
* Role-Based Authorization
* Password Encryption

### 📅 Event Management

* Create Event
* Update Event
* Publish Event
* Soft Delete
* Event Details
* Pagination & Filtering

### 🎟 Registration

* Event Registration
* Duplicate Registration Prevention
* Capacity Validation
* Transaction Management
* Pessimistic Locking
* Registration Cancellation
* Automatic Waitlist Promotion

### 🎫 Ticket Management

* Automatic Ticket Generation
* Unique Ticket Number
* QR Code Generation
* QR-Based Check-In
* Duplicate Check-In Prevention

### 📊 Organizer Dashboard

* Registration Statistics
* Cancelled Registrations
* Checked-In Users
* Waitlisted Users
* Available Seats

### 📧 Notifications

* Registration Confirmation Email
* Cancellation Email
* Waitlist Promotion Email
* HTML Email Templates
* Asynchronous Email Sending

---

## 🛠 Tech Stack

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA (Hibernate)
* MySQL
* Maven
* JWT
* JavaMailSender
* Swagger / OpenAPI

---

## 🏗 Architecture

The project follows a layered architecture:

* Controller Layer
* Service Layer
* Repository Layer
* Entity Layer
* DTO Layer
* Exception Handling Layer

---

## 📷 Screenshots

> Add screenshots inside the `screenshots` folder.

* Login
* Register
* Event Creation
* Organizer Dashboard
* Swagger UI
* QR Ticket
* Email Notification

---

## 🚀 Running the Project

1. Clone the repository.
2. Configure MySQL.
3. Update `application.properties`.
4. Run the Spring Boot application.
5. Open Swagger and test the APIs.

---

## Key Learning Outcomes

- Built secure REST APIs using Spring Security and JWT.
- Implemented transactional business logic with pessimistic locking.
- Designed QR-based ticket generation and event check-in.
- Developed waitlist promotion and organizer analytics.
- Integrated asynchronous HTML email notifications.
- Applied layered architecture and clean code principles.

## 🎯 Future Improvements

* Docker Support
* AWS Deployment
* Angular Frontend
* Unit Testing
* Integration Testing
* Kafka-Based Notification Service
* Microservices Architecture

---

## 👨‍💻 Author

Developed as a portfolio project to demonstrate enterprise backend development using Spring Boot and modern Java.
