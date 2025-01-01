# E-Commerce Application
This is a simple e-commerce application built using Spring Boot, Java 17, PostgreSQL, and JPA. The project is designed to practice concepts such as unit testing, CI/CD integration, and containerization with Docker.
It includes features like user registration and management, with unit tests implemented for the service and controller layers.

## Features
- **User Management**: User registration and management functionality.
-**Unit Testing**: Unit tests to ensure reliability and quality of code.
-**CI/CD Integration**: Jenkins integration triggered via WebHooks when changes are pushed to the main branch.
-**Docker**: Dockerized application and PostgreSQL database for easy deployment and scalability.
-**Jenkins**: Continuous integration using Jenkins with Maven for build automation.

## Tech Stack
-**Backend**: Spring Boot, Java 17
-**Database**: PostgreSQL
-**ORM**: JPA (Java Persistence API)
-**Testing**: JUnit
-**CI/CD**: Jenkins, WebHooks, Maven
-**Containerization**: Docker
-**Version Control**: GitHub

## Getting Started
Prerequisites
Before you can run this project locally, make sure you have the following installed:

-Java 17
-Docker (for running the application and database in containers)
-PostgreSQL (if not using Docker for the database)
-Jenkins (for build automation, if using Jenkins on your local machine)
-ngrok (for exposing Jenkins locally to the public)
