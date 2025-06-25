package com.projects.my.brooker_api_test_automation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class PingTest extends BaseTest{

    @BeforeEach
    public void beforeEach(){
        realizarLogin();
    }

    @Test
    public void deveVerificaIntegridadeApiEmExecucao(){
        given()
                .accept("application/json")
                .log().all()
                .when()
                .get("/ping")
                .then()
                .log().all()
                .statusCode(200);   //BUG ENCONTRADO - RETORNAR STATUS CODE 200 MAS DEVERIA RETORNAR 201
    }
}
