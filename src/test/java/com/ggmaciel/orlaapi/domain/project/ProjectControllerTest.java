package com.ggmaciel.orlaapi.domain.project;

import com.ggmaciel.orlaapi.AbstractDatabaseIntegrationConfiguration;
import com.ggmaciel.orlaapi.domain.employee.Employee;
import com.ggmaciel.orlaapi.domain.project.dto.CreateProjectDTO;
import com.ggmaciel.orlaapi.exception.EntityAlreadyExistsException;
import com.ggmaciel.orlaapi.exception.EntityNotFoundException;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ggmaciel.orlaapi.helpers.ConstantHelper.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectControllerTest extends AbstractDatabaseIntegrationConfiguration {
    private final String BASE_PATH = "/v1/project";
    private final String CONTENT_TYPE = "application/json";

    @MockBean
    private ProjectService projectService;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldReturn400WhenNameHasLessThan1Character() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"name\": \"\"}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(400)
                .body("name", equalTo(INVALID_NAME_SIZE));
    }

    @Test
    void shouldReturn400WhenNameHasMoreThan255Characters() {
        String name = " ".repeat(256);

        given()
                .contentType(CONTENT_TYPE)
                .body(String.format("{\"name\": \"%s\"}", name))
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(400)
                .body("name", equalTo(INVALID_NAME_SIZE));
    }

    @Test
    void shouldReturn200WhenProjectIsCreatedSuccessfully() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"name\": \"Projeto 1\"}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(200);
    }

    @Test
    void shouldReturn409WhenProjectAlreadyExists() {
        when(projectService.create(any(CreateProjectDTO.class))).thenThrow(new EntityAlreadyExistsException(PROJECT_ALREADY_EXISTS));

        given()
                .contentType(CONTENT_TYPE)
                .body("{\"name\": \"Projeto 1\"}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(409)
                .body("error", equalTo(PROJECT_ALREADY_EXISTS));
    }

    @Test
    void shouldReturn400WhenNameIsNull() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"name\": null}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(400)
                .body("name", equalTo(INVALID_NAME_SIZE));
    }

    @Test
    void shouldReturn200AndListOfProjectsWithRespectiveEmployees() {
        Project project = new Project("Project 01");
        when(projectService.findProjectsWithRespectiveEmployees()).thenReturn(List.of(project));

        given()
                .contentType(CONTENT_TYPE)
                .when()
                .get(BASE_PATH + "/with-employees")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].name", equalTo(project.getName()));
    }

    @Test
    void shouldReturn200AndEmptyListWhenNoProjectsWithRespectiveEmployeesExist() {
        when(projectService.findProjectsWithRespectiveEmployees()).thenReturn(Collections.emptyList());

        given()
                .contentType(CONTENT_TYPE)
                .when()
                .get(BASE_PATH + "/with-employees")
                .then()
                .statusCode(200)
                .body("$", empty());
    }

    @Test
    void shouldReturn200AndSetOfEmployeesWhenProjectIdExists() {
        Long projectId = 1L;
        Employee employee = new Employee();
        Set<Employee> employees = new HashSet<>(Collections.singletonList(employee));
        when(projectService.findEmployeesByProjectId(projectId)).thenReturn(employees);

        given()
                .contentType(CONTENT_TYPE)
                .when()
                .get(BASE_PATH + "/" + projectId + "/employees")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].name", equalTo(employee.getName()));
    }

    @Test
    void shouldReturn200AndEmptySetWhenProjectHasNoEmployees() {
        Long projectId = 1L;
        when(projectService.findEmployeesByProjectId(projectId)).thenReturn(Collections.emptySet());

        given()
                .contentType(CONTENT_TYPE)
                .when()
                .get(BASE_PATH + "/" + projectId + "/employees")
                .then()
                .statusCode(200)
                .body("$", empty());
    }

    @Test
    void shouldReturn404WhenProjectIdDoesNotExist() {
        Long projectId = 1L;
        when(projectService.findEmployeesByProjectId(projectId)).thenThrow(new EntityNotFoundException(ENTITY_NOT_FOUND));

        given()
                .contentType(CONTENT_TYPE)
                .when()
                .get(BASE_PATH + "/" + projectId + "/employees")
                .then()
                .statusCode(404)
                .body("error", equalTo(ENTITY_NOT_FOUND));
    }
}