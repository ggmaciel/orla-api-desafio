package com.ggmaciel.orlaapi.domain.employee;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.ggmaciel.orlaapi.helpers.ConstantHelper.INVALID_CPF_SIZE;
import static com.ggmaciel.orlaapi.helpers.ConstantHelper.INVALID_NAME_SIZE;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerTest {
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
    void shouldReturn400WhenNameHasLessThan1Character() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"name\": \"\", \"email\": \"mail@mail.com\", \"cpf\": \"12345678798\", \"salary\": 0}")
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
                .body("{\"name\": \"John Doe\", \"cpf\": \"123\"}")
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
                .body("{\"name\": \"John Doe\", \"cpf\": \"1234567890112345\"}")
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
                .body("{\"name\": \"John Doe\", \"cpf\": null}")
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(400)
                .body("cpf", equalTo(INVALID_CPF_SIZE));
    }
}