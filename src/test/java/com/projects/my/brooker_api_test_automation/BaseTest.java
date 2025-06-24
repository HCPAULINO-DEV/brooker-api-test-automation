package com.projects.my.brooker_api_test_automation;

import io.restassured.RestAssured;
import org.apache.coyote.Response;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected static String token;

    @BeforeAll
    protected static void realizarLogin(){
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        String json = """
                {
                                    "username" : "admin",
                                    "password" : "password123"
                                }
                """;

        String token =
                given()
                        .contentType("application/json")
                        .body(json)
                        .when()
                        .post("/auth")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("token");
    }
}
