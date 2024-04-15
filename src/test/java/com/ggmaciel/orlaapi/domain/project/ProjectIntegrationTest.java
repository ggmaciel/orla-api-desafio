package com.ggmaciel.orlaapi.domain.project;

import com.ggmaciel.orlaapi.domain.employee.Employee;
import com.ggmaciel.orlaapi.domain.employee.EmployeeRepository;
import io.restassured.RestAssured;
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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectIntegrationTest {
    private final String BASE_PATH = "/v1/project";
    private final String CONTENT_TYPE = "application/json";

    @LocalServerPort
    private Integer port;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    EmployeeRepository employeeRepository;

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
    void shouldCreateAProject() {
        String projectName = "Project 1";

        given()
                .contentType(CONTENT_TYPE)
                .body("{\"name\": \"Project 1\"}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(200);

        Project actualProject = projectRepository.findById(1L).orElseThrow();

        assertEquals(1L, actualProject.getId());
        assertEquals(projectName, actualProject.getName());
    }

    @Test
    void shouldReturnErrorWhenProjectWithNameAlreadyExists() {
        Project project = new Project("Project 1");
        projectRepository.save(project);

        given()
                .contentType(CONTENT_TYPE)
                .body("{\"name\": \"Project 1\"}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(409);
    }

    @Test
    void shouldFindProjectsWithRespectiveEmployees() {
        Employee employee1 = employeeRepository.save(new Employee("12345678901", "email@mail.com", "Test", 1000.0));
        Employee employee2 = employeeRepository.save(new Employee("12345678902", "mail@email.com", "Test", 1000.0));

        Project project1 = projectRepository.save(new Project("Project 1"));
        Project project2 = projectRepository.save(new Project("Project 2"));

        employee1.getProjects().add(project1);
        employee1.getProjects().add(project2);
        employeeRepository.save(employee1);

        employee2.getProjects().add(project1);
        employeeRepository.save(employee2);

        given()
                .contentType(CONTENT_TYPE)
                .when()
                .get(BASE_PATH + "/with-employees")
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("id", containsInAnyOrder(project1.getId().intValue(), project2.getId().intValue()))
                .body("name", containsInAnyOrder(project1.getName(), project2.getName()))
                .body("findAll { it.employees.size() == 2 }.employees.id.flatten()", containsInAnyOrder(employee1.getId().intValue(), employee2.getId().intValue()))
                .body("findAll { it.employees.size() == 1 }.employees.id.flatten()", containsInAnyOrder(employee1.getId().intValue()));
    }
}
