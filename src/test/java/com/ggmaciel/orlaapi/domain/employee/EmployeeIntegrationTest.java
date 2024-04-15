package com.ggmaciel.orlaapi.domain.employee;

import com.ggmaciel.orlaapi.domain.project.ProjectRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeIntegrationTest {
    private final String BASE_PATH = "/v1/employee";
    private final String CONTENT_TYPE = "application/json";

    @LocalServerPort
    private Integer port;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ProjectRepository projectRepository;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        employeeRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Test
    @Transactional
    void shouldCreateAEmployee() {
        Employee expectedEmployee = new Employee("12345678901", "email@mail.com", "Test", 1000.0);
        expectedEmployee.setId(1L);

        given()
                .contentType(CONTENT_TYPE)
                .body("{\"name\": \"Test\", \"cpf\": \"12345678901\", \"email\": \"email@mail.com\", \"salary\": 1000}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(200);

        Employee actualEmployee = employeeRepository.findById(1L).orElseThrow();

        assertEquals(expectedEmployee, actualEmployee);
    }

    @Test
    void shouldReturnErrorWhenEmployeeWithCpfAlreadyExists() {
        Employee employee = new Employee("12345678901", "email@mail.com", "Test", 1000.0);
        employeeRepository.save(employee);

        given()
                .contentType(CONTENT_TYPE)
                .body("{\"name\": \"Test\", \"cpf\": \"12345678901\", \"email\": \"mail@mail.com\", \"salary\": 1000}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(409);
    }

    @Test
    void shouldReturnErrorWhenEmployeeWithEmailAlreadyExists() {
        Employee employee = new Employee("12345678903", "email@mail.com", "Test", 1000.0);
        employeeRepository.save(employee);

        given()
                .contentType(CONTENT_TYPE)
                .body("{\"name\": \"Test\", \"cpf\": \"12345678901\", \"email\": \"email@mail.com\", \"salary\": 1000}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(409);
    }
}
