package com.ggmaciel.orlaapi.domain.project;

import com.ggmaciel.orlaapi.domain.project.dto.CreateProjectDTO;
import com.ggmaciel.orlaapi.exception.EntityAlreadyExistsException;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.ggmaciel.orlaapi.helpers.ConstantHelper.INVALID_NAME_SIZE;
import static com.ggmaciel.orlaapi.helpers.ConstantHelper.PROJECT_ALREADY_EXISTS;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectControllerTest {
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
}