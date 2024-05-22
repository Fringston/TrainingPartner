# TrainingPartner
Authenticated backend API for the mobile app TrainingPartner that helps you with your training.

---

## Description

This project is a backend API for the mobile app TrainingPartner. 
The API is built with Spring Boot and uses Spring Security for authentication. It is connected to a MySQL database and uses JPA for data access.

The API is documented with OpenAPI and the documentation can be accessed at `http://localhost:8000/swagger-ui.html`.

---
## Tech Stack

The following tools has been used in the building of this project:
- Framework: Spring Boot
- Database: MySQL
- Object-Relational Mapping (ORM) Tool: Spring Data JPA
- Authentication: Spring Security
- Testing: JUnit and Mockito
- Build Tool: Maven
- Continuous Integration (CI): GitHub Actions
- Documentation: OpenAPI
- Other Tools: Project Lombok, Spring Boot DevTools, Spring Boot Starter OAuth2 Resource Server, Springdoc OpenAPI UI.

---

## Installation

### Make sure you have the following installed on your local machine:
- [Git](https://git-scm.com/)
- [MySQL](https://www.mysql.com/)
- An IDE, for example [IntelliJ IDEA](https://www.jetbrains.com/idea/)

---

## Usage

### 1. Set up the MySQL database:
- Open MySQL Workbench.
- Connect to your MySQL server.
- Create a new schema for the application. You can do this by clicking on the "Create a new schema in the connected server" button, entering a name for the schema, and clicking "Apply".
- Ensure that the *schema name*, *username*, and *password* in the application.properties file match your MySQL setup.

### 2. Start the backend application:
- Clone the [repository](https://github.com/Fringston/TrainingPartner) to your local machine
- Open the project in your preferred IDE 
- Run the project

### 3. Open and run the frontend application.

---

## Continuous Integration (CI)

This project uses [GitHub Actions](https://github.com/features/actions) for Continuous Integration. 
CI helps automate the process of building, testing, and deploying the application whenever there is a change to the source code.

In this project, a workflow is triggered on every `push` event to the `main` and `dev` branches. 
The workflow includes step s to set up JDK 17, build the project withMaven, and run tests.

You can view the configuration for this workflow in the [`.github/workflows/maven.yml`](.github/workflows/maven.yml) file.

---

## Dependencies

- [Spring Boot Starter Data JPA](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa)
- [Spring Boot Starter Web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web)
- [Spring Boot DevTools](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools)
- [MySQL Connector Java](https://mvnrepository.com/artifact/mysql/mysql-connector-java)
- [Project Lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok)
- [Spring Boot Starter Test](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test)
- [Spring Boot Starter Security](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security)
- [Spring Boot Starter OAuth2 Resource Server](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-oauth2-resource-server)
- [JUnit](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api)
- [Mockito](https://mvnrepository.com/artifact/org.mockito/mockito-core)
- [Springdoc OpenAPI UI](https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-ui)

---

## Credits

I received help and inspiration from the following sources:
- **[Unknown Koder](https://github.com/unknownkoder/spring-security-login-system)** - Base for the authentication setup.
- **GitHub Copilot** - Facilitate troubleshooting and provide architectural suggestions.

---

## License

- [MIT License](https://choosealicense.com/licenses/mit/)

---

## Tests

The tests has been written with JUnit and Mockito.

### Running the tests

To run all the tests, right-click on the test folder **com.fredrikkodar.TrainingPartner** 
and select **"Run Tests in com.fredrikkodar.TrainingPartner"**.

---
