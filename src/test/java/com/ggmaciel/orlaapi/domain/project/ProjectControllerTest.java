package com.ggmaciel.orlaapi.domain.project;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.ggmaciel.orlaapi.helpers.ConstantHelper.INVALID_NAME_SIZE;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

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
}