package com.ggmaciel.orlaapi.domain.employee;

import com.ggmaciel.orlaapi.AbstractDatabaseIntegrationConfiguration;
import com.ggmaciel.orlaapi.domain.employee.dto.AddProjectDTO;
import com.ggmaciel.orlaapi.domain.employee.dto.CreateEmployeeDTO;
import com.ggmaciel.orlaapi.domain.employee.dto.EmployeeProjectDTO;
import com.ggmaciel.orlaapi.exception.EntityAlreadyExistsException;
import com.ggmaciel.orlaapi.exception.EntityNotFoundException;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.ggmaciel.orlaapi.helpers.ConstantHelper.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerTest extends AbstractDatabaseIntegrationConfiguration {
    private final String BASE_PATH = "/v1/employee";
    private final String CONTENT_TYPE = "application/json";

    @MockBean
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldReturn200WhenEmployeeIsCreatedSuccessfully() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"name\": \"Test\", \"cpf\": \"12345678901\", \"email\": \"email@mail.com\", \"salary\": 1000}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(200);
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
    void shouldReturn400WhenCpfHasLessThan11Characters() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"cpf\": \"123\"}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(400)
                .body("cpf", equalTo(INVALID_CPF_SIZE));
    }

    @Test
    void shouldReturn400WhenCpfHasMoreThan11Characters() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"cpf\": \"1234567890112345\"}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(400)
                .body("cpf", equalTo(INVALID_CPF_SIZE));
    }

    @Test
    void shouldReturn400WhenCpfIsNull() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"cpf\": null}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(400)
                .body("cpf", equalTo(INVALID_CPF_SIZE));
    }

    @Test
    void shouldReturn400WhenEmailHasMoreThan255Characters() {
        String username = "a".repeat(64);
        String domain = "b".repeat(189) + ".com";
        String email = username + "@" + domain;

        given()
                .contentType(CONTENT_TYPE)
                .body(String.format("{\"email\": \"%s\"}", email))
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(400)
                .body("email", equalTo(INVALID_EMAIL));
    }

    @Test
    void shouldReturn400WhenEmailDoesNotHaveAtSign() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"email\": \"mailmail.com\"}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(400)
                .body("email", equalTo(INVALID_EMAIL));
    }

    @Test
    void shouldReturn400WhenEmailHasMultiplesAtSigns() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"email\": \"test@@example.com\"}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(400)
                .body("email", equalTo(INVALID_EMAIL));
    }

    @Test
    void shouldReturn400WhenEmailDoesNotHaveADomain() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"email\": \"test@\"}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(400)
                .body("email", equalTo(INVALID_EMAIL));
    }

    @Test
    void shouldReturn400WhenEmailDoesNotHaveAUsername() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"email\": \"@example.com\"}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(400)
                .body("email", equalTo(INVALID_EMAIL));
    }

    @Test
    void shouldReturn400WhenEmailIsNull() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"email\": null}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(400)
                .body("email", equalTo(INVALID_EMAIL));
    }

    @Test
    void shouldReturn400WhenSalaryIsNegative() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"salary\": -1}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(400)
                .body("salary", equalTo(INVALID_SALARY));
    }

    @Test
    void shouldReturn404WhenAEntityIsNotFound() {
        doThrow(new EntityNotFoundException(ENTITY_NOT_FOUND)).when(employeeService).addProject(any(AddProjectDTO.class));

        given()
                .contentType(CONTENT_TYPE)
                .body("{\"employeeId\": 1, \"projectId\": 1}")
                .when()
                .post(BASE_PATH + "/add-project")
                .then()
                .statusCode(404)
                .body("error", equalTo(ENTITY_NOT_FOUND));
    }

    @Test
    void shouldReturn400WhenEmployeeIdIsNull() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"employeeId\": null, \"projectId\": 1}")
                .when()
                .post(BASE_PATH + "/add-project")
                .then()
                .statusCode(400)
                .body("employeeId", equalTo(ID_MUST_BE_A_VALID_NUMBER));
    }

    @Test
    void shouldReturn400WhenProjectIdIsNull() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"employeeId\": 1, \"projectId\": null}")
                .when()
                .post(BASE_PATH + "/add-project")
                .then()
                .statusCode(400)
                .body("projectId", equalTo(ID_MUST_BE_A_VALID_NUMBER));
    }

    @Test
    void shouldReturn400WhenEmployeeIdIsNegative() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"employeeId\": -1, \"projectId\": 1}")
                .when()
                .post(BASE_PATH + "/add-project")
                .then()
                .statusCode(400)
                .body("employeeId", equalTo(ID_MUST_BE_A_VALID_NUMBER));
    }

    @Test
    void shouldReturn400WhenProjectIdIsNegative() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"employeeId\": 1, \"projectId\": -1}")
                .when()
                .post(BASE_PATH + "/add-project")
                .then()
                .statusCode(400)
                .body("projectId", equalTo(ID_MUST_BE_A_VALID_NUMBER));
    }

    @Test
    void shouldReturn200WhenProjectIsAddedSuccessfully() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"employeeId\": 1, \"projectId\": 1}")
                .when()
                .post(BASE_PATH + "/add-project")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldReturn409WhenEmployeeWithCpfAlreadyExists() {
        when(employeeService.create(any(CreateEmployeeDTO.class))).thenThrow(new EntityAlreadyExistsException(EMPLOYEE_WITH_CPF_ALREADY_EXISTS));

        given()
                .contentType(CONTENT_TYPE)
                .body("{\"name\": \"Test\", \"cpf\": \"12345678901\", \"email\": \"email@mail.com\", \"salary\": 1000}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(409)
                .body("error", equalTo(EMPLOYEE_WITH_CPF_ALREADY_EXISTS));
    }

    @Test
    void shouldReturn409WhenEmployeeWithEmailAlreadyExists() {
        when(employeeService.create(any(CreateEmployeeDTO.class))).thenThrow(new EntityAlreadyExistsException(EMPLOYEE_WITH_EMAIL_ALREADY_EXISTS));

        given()
                .contentType(CONTENT_TYPE)
                .body("{\"name\": \"Test\", \"cpf\": \"12345678901\", \"email\": \"email@mail.com\", \"salary\": 1000}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(409)
                .body("error", equalTo(EMPLOYEE_WITH_EMAIL_ALREADY_EXISTS));
    }

    @Test
    void shouldReturn200AndProjectsWhenEmployeeIdExists() {
        Long employeeId = 1L;
        EmployeeProjectDTO projectDTO = new EmployeeProjectDTO(1L, "Project 01", new Date());
        Set<EmployeeProjectDTO> dto = new HashSet<>(Set.of(projectDTO));
        when(employeeService.findProjectsByEmployeeId(employeeId)).thenReturn(dto);

        given()
                .contentType(CONTENT_TYPE)
                .when()
                .get(BASE_PATH + "/" + employeeId + "/projects")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].id", equalTo(projectDTO.getId().intValue()))
                .body("[0].name", equalTo(projectDTO.getName()));
    }

    @Test
    void shouldReturn200AndEmptySetWhenEmployeeHasNoProjects() {
        Long employeeId = 1L;
        when(employeeService.findProjectsByEmployeeId(employeeId)).thenReturn(Collections.emptySet());

        given()
                .contentType(CONTENT_TYPE)
                .when()
                .get(BASE_PATH + "/" + employeeId + "/projects")
                .then()
                .statusCode(200)
                .body("$", empty());
    }

    @Test
    void shouldReturn404WhenEmployeeIdDoesNotExist() {
        Long employeeId = 1L;
        when(employeeService.findProjectsByEmployeeId(employeeId)).thenThrow(new EntityNotFoundException(ENTITY_NOT_FOUND));

        given()
                .contentType(CONTENT_TYPE)
                .when()
                .get(BASE_PATH + "/" + employeeId + "/projects")
                .then()
                .statusCode(404)
                .body("error", equalTo(ENTITY_NOT_FOUND));
    }
}