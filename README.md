# TrainingPartner
Authenticated backend API for the mobile app TrainingPartner that helps you with your training.

---

## Description

This project is a backend API for the mobile app TrainingPartner. 
The API is built with Spring Boot and uses Spring Security for authentication. It is connected to a MySQL database and uses JPA for data access. 
The API is documented with OpenAPI and the documentation can be accessed at `http://localhost:8000/swagger-ui.html`.

---

## Installation

1. Download and install MySQL and MySQL Workbench on your local machine.
2. Clone this repository to your local machine.

---

## Usage

### 1. Set up the MySQL database:
- Open MySQL Workbench.
- Connect to your MySQL server.
- Create a new schema for the application. You can do this by clicking on the "Create a new schema in the connected server" button, entering a name for the schema, and clicking "Apply".
- Ensure that the *schema name*, *username*, and *password* in the application.properties file match your MySQL setup.

### 2. Start the backend application:
1. Open the project in your preferred IDE
2. Run the project
3. The API is now running on `http://localhost:8000`

### 3. Open and run the frontend application.

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
- [Unknown Koder](https://www.youtube.com/watch?v=TeBt0Ike_Tk)

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
