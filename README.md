# Real Estate Listings and Management System With Spring Boot 3.0 Security and JWT Implementation

This project demonstrates a real estate backend with AWS s3 bucket storage (images), and authentification and authoriazation with the implementations of spring security using Spring Boot 3.0 and JSON Web Tokens (JWT).

This is a web-based application that allows real estate agents to create, manage, and share listings of properties with potential clients. It also provides tools for managing client relationships, scheduling property viewings, and tracking leads. It includes the following features:

## Features

- CRUD for read write create update and delete properties
  Agent Controller
- Create agent account
- Authentication and Authorization
- AWS s3 Bucket for image storage

Real estate

- Load real estate by id - in relation with the uploaded agent
- Load real estate list (with customized real estate DTO)
- Load real estate list by status (similar listing)
- Upload Real Estate Images to AWS s3 Bucket
- Update real estate
- Delete real estate
- Find Real Estate By Province, District

Security

- User registration and login with JWT authentication
- Password encryption using BCrypt
- Role-based authorization with Spring Security
- Customized access denied handling
- Logout mechanism
- Refresh token

## Technologies

- Spring Boot 3.0
- Spring Security
- JSON Web Tokens (JWT)
- BCrypt
- Maven
- Lombok
- Swagger

Accessing Swagger Documentation

- Start the Spring Boot application.
- Open your preferred web browser and go to http://localhost:8080/swagger-ui/index.html#/.
- You should now be redirected to the Swagger documentation for your API.
- From here, you can explore the available API endpoints, read the API documentation, and even try out the endpoints directly from the Swagger UI.
- If you want to access the Swagger API definition in JSON format, go to http://localhost:8080/v2/api-docs.

## Getting Started

To get started with this project, you will need to have the following installed on your local machine:

- JDK 17+
- MySql
- Maven 3+

To build and run the project, follow these steps:

- Clone the repository: `git clone https://github.com/huylam22/real-estate-server-update`
- Navigate to the project directory: cd real-estate-backend
- Build the project: mvn clean install
- Run the project: mvn spring-boot:run

-> The application will be available at http://localhost:8080.

## Properties

- PROD_DB_HOST= YOUR DB HOST
- PROD_DB_PORT= DB Your DB PORT
- PROD_DB_NAME= railway
- PROD_DB_USERNAME= Your DB USERNAME
- PROD_DB_PASSWORD= Your DB PASSWORD
- cloud.aws.credentials.access-key= Your AWS ACCESS KEY ID
- cloud.aws.credentials.secret-key= Your AWS SECRET KEY

## Acknowledgement

We would like to thank the Spring community for providing a robust and flexible framework for building web applications. We would also like to thank our mentors and advisors for their guidance and support throughout the development process.

## References

Spring Security:
@BoualiAli Youtube Chanel
Git: https://github.com/ali-bouali
Security Project: https://github.com/ali-bouali/spring-boot-3-jwt-security

Real Estate Photo Upload: https://github.com/arnaugarcia/realstatecamp
