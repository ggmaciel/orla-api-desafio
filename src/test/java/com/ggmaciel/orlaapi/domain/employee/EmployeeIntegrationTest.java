package com.ggmaciel.orlaapi.domain.employee;

import com.ggmaciel.orlaapi.domain.project.Project;
import com.ggmaciel.orlaapi.domain.project.ProjectRepository;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
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

    @Autowired
    EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        jdbcTemplate.execute("TRUNCATE TABLE employee_project, employee, project RESTART IDENTITY CASCADE");
    }

    @Test
    void shouldCreateAEmployee() {
        Employee expectedEmployee = new Employee("12345678901", "email@mail.com", "Test", 1000.0);

        given()
                .contentType(CONTENT_TYPE)
                .body("{\"name\": \"Test\", \"cpf\": \"12345678901\", \"email\": \"email@mail.com\", \"salary\": 1000}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(200);

        expectedEmployee.setId(1L);

        Employee actualEmployee = employeeRepository.findById(expectedEmployee.getId()).orElseThrow();

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

    @Test
    void shouldAddProjectToEmployee() {
        Employee employee = new Employee("12345678901", "email@mail.com", "Test", 1000.0);
        employee = employeeRepository.save(employee);

        Project project = new Project("Project Test");
        project = projectRepository.save(project);

        given()
                .contentType(CONTENT_TYPE)
                .body("{\"employeeId\": 1, \"projectId\": 1}")
                .when()
                .post(BASE_PATH + "/add-project")
                .then()
                .statusCode(200);

        // Avoid LazyInitializationException - https://vladmihalcea.com/the-best-way-to-handle-the-lazyinitializationexception/
        List<Long> projectIds = ((List<?>) entityManager
                .createNativeQuery("SELECT project_id FROM employee_project WHERE employee_id = :id")
                .setParameter("id", employee.getId())
                .getResultList())
                .stream()
                .map(obj -> ((Number) obj).longValue())
                .toList();

        assertEquals(projectIds.getFirst(), project.getId());
        assertEquals(1, projectIds.size());
    }

    @Test
    void shouldFindProjectsByEmployeeId() {
        Employee employee = new Employee("12345678901", "email@mail.com", "Test", 1000.0);
        employee = employeeRepository.save(employee);

        Project project1 = new Project("Project Test");
        project1 = projectRepository.save(project1);
        employee.getProjects().add(project1);

        Project project2 = new Project("Project Test 2");
        project2 = projectRepository.save(project2);
        employee.getProjects().add(project2);

        employeeRepository.save(employee);

        given()
                .contentType(CONTENT_TYPE)
                .when()
                .get(BASE_PATH + "/" + employee.getId() + "/projects")
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("id", containsInAnyOrder(project1.getId().intValue(), project2.getId().intValue()))
                .body("name", containsInAnyOrder(project1.getName(), project2.getName()));
    }

    @Test
    void shouldReturnEmptyListWhenEmployeeHasNoProjects() {
        Employee employee = new Employee("12345678901", "email@mail.com", "Test", 1000.0);
        employee = employeeRepository.save(employee);

        given()
                .contentType(CONTENT_TYPE)
                .when()
                .get(BASE_PATH + "/" + employee.getId() + "/projects")
                .then()
                .statusCode(200)
                .body("$", hasSize(0));
    }
}
