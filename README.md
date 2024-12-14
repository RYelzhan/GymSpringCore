# GymSpringCore

First Project in EPAM Java Internship.

Project where we try to create an application with console interface. It will be a gym CRM, where users can register as trainee or trainer.
Several Service classes that will work with Repository/Dao objects. Our tool is Spring Core, so dependency injection should not be a problem.

Database is basically a file which gets read with every start. After reading it, I will store all the users, trainings and other objects in heap memory of computer. The datastucture used to store is Map<>, I will use HashMap<> as it's amortised complexity is smaller than other implementations. Entities used in app have pretty simple relationship. All trainees and trainers are generalisation of User. Training depends on Trainee and Trainer. Training type depends on Training and Trainer.

The main challenge I will probably face is how to make program keep running, when I do not have a Spring Boot. But I already have an Idea.

docker run --detach --name mycontainer -p 61616:61616 -p 8161:8161 --rm apache/activemq-artemis:latest-alpine

## Overview

The Gym-CRM application is a comprehensive system designed to manage gym operations, including users, trainers, trainees, training types, and training sessions.

## Architecture

The Gym-CRM system follows a microservices-based architecture with the following components:

### 1.  Spring API Gateway:

- Acts as a single entry point to the application with load balancing.

- Interacts with the Authentication Service to ensure all requests to services are authenticated.

### 2. Service Discovery:

- Responsible for registering services so the API Gateway can redirect requests to services by their registered names instead of hardcoded URLs.

### 3. Authentication Service:

- Handles authentication and JWT token creation.

- API Gateway sends requests to authenticate users.

- Provides authenticated usernames to the API Gateway for forwarding to other services.

- Offers endpoints for users to interact via the API Gateway to obtain tokens for streamlined authentication.

### 4. Gym Service:

- Manages trainees and trainers' accounts.

- Allows users to register, create trainings, and filter trainings by criteria.

- Maintains training types and schedules.

### 5. Statistics Service:

- Collects and manages training session data for trainers, including the number of minutes spent in trainings for each month of different years.

- Receives data asynchronously from the Gym Service via Active MQ Artemis.

- Provides HTTP endpoints for querying statistics.

### 6. Gym Common:

- Not a runnable service but a shared library module.

- Contains DTOs and constants used for communication between services (e.g., between Gym and Statistics Services, Authentication and Gym Services).

### 7. Communication:

- Synchronous communication via REST endpoints.

- Asynchronous communication using Active MQ Artemis.

## Technology Stack

- Language: Java

- Frameworks: Spring Boot, Spring Cloud

- Database:

  - PostgreSQL (Gym Service)

  - H2 (Authentication and Statistics Services)

- Messaging: Active MQ Artemis

- Service Discovery: Spring Cloud Netflix Eureka (Client and Server)

- API Gateway: Spring Cloud Gateway

## Setup

### Prerequisites:

- JDK 17

- Maven

- Docker Desktop

- Active MQ Artemis

- Grafana and Prometheus

## Instructions

### 1. Clone the repository:

  ```bash
  git clone https://github.com/RYelzhan/GymSpringCore.git
  cd GymSpringCore
  ```

### 2. Configure services:

- Update application.properties files in each service with appropriate database and Active MQ configurations.

### 3. Start services:

- Navigate to each service module and run:

  ```bash
  mvn spring-boot:run
  ```

- Start the Service Discovery application first, followed by other services in any order.

### 4. Start Active MQ Artemis:

- Ensure Active MQ Artemis is running on tcp:61616 (default port).

- To start using Docker:

  ```bash
  docker run --detach --name mycontainer -p 61616:61616 -p 8161:8161 --rm apache/activemq-artemis:latest-alpine
  ```

### 5. Start Monitoring Services:

- A docker-compose.yml file for Grafana and Prometheus is located in the monitoring directory of the Gym Main module.

### 6. Start Zipkin Tracing Services:
- Ensure Zipkin Tracing is running on 
- To start using Docker:
  ```bash
  docker run -d -p 9411:9411 openzipkin/zipkin:latest
  ```

## Configuration Details

- All configurations can be updated in application.properties files for each module.

- ### Environment Variables:

  - Database username: ${POSTGRESQL_USERNAME}

  - Database password: ${POSTGRESQL_PASSWORD}

- ### Logging:

  - Configure using logback-spring.xml or application.properties files.

