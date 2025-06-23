package com.projects.my.brooker_api_test_automation;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class LoginTest {

    @BeforeAll
    static void beforeAll(){
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
    }

    @Test
    public void deveRealizarLogin(){
        String json = """
                {
                    "username" : "admin",
                    "password" : "password123"
                }
                """;

        given()
                .contentType("application/json")
                .body(json)
                .log().all()
                .when()
                .post("/auth")
                .then()
                .log().all()
                .statusCode(200)
                .body("token", notNullValue());
    }

}
