package com.projects.my.brooker_api_test_automation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

public class BookingTest extends BaseTest{

    @BeforeEach
    public void beforeEach(){
        realizarLogin();
    }

    @Test
    public void deveRetornarIdsDeTodasReservas(){
        String json = """
                    {
                        "firstname" : "Jim",
                        "lastname" : "Brown",
                        "totalprice" : 111,
                        "depositpaid" : true,
                        "bookingdates" : {
                            "checkin" : "2018-01-01",
                            "checkout" : "2019-01-01"
                        },
                        "additionalneeds" : "Breakfast"
                    }
                """;

        //EFETUAR RESERVA
        given()
                .contentType("application/json")
                .body(json)
                .header("Cookie", "token=" + token)
                .log().all()
                .when()
                .post("/booking")
                .then()
                .log().all()
                .statusCode(200);

        //RETORNAR IDS RESERVAS
        given()
                .accept("application/json")
                .header("Cookie", "token=" + token)
                .log().all()
                .when()
                .get("/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("bookingid", not(empty()));


    }
}
