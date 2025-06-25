package com.projects.my.brooker_api_test_automation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BookingTest extends BaseTest{

    @BeforeEach
    public void beforeEach(){
        realizarLogin();
    }

    private record ReservaJson(String json, String firstname, String lastname, Integer totalprice, Boolean depositpaid){}

    private ReservaJson jsonReserva(){
        String firstname = "Jim";
        String lastname = "Brown";
        Integer totalprice = 111;
        Boolean depositpaid = true;

        String json = """
                    {
                        "firstname" : "%s",
                        "lastname" : "%s",
                        "totalprice" : %s,
                        "depositpaid" : %s,
                        "bookingdates" : {
                            "checkin" : "2018-01-01",
                            "checkout" : "2019-01-01"
                        },
                        "additionalneeds" : "Breakfast"
                    }
                """.formatted(firstname, lastname, totalprice, depositpaid);

        return new ReservaJson(json, firstname, lastname, totalprice, depositpaid);
    }

    @Test
    public void deveRetornarIdsDeTodasReservas(){
        ReservaJson reserva = jsonReserva();

        //EFETUAR RESERVA
        given()
                .contentType("application/json")
                .body(reserva.json)
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

    @Test
    public void deveRetornarReservaPeloId(){
        ReservaJson reserva = jsonReserva();

        //EFETUAR RESERVA
        Integer idReserva =
        given()
                .contentType("application/json")
                .body(reserva.json)
                .header("Cookie", "token=" + token)
                .log().all()
                .when()
                .post("/booking")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .path("bookingid");

        //RETORNAR RESERVA PELO ID
        given()
                .accept("application/json")
                .header("Cookie", "token=" + token)
                .log().all()
                .when()
                .get("/booking/" + idReserva)
                .then()
                .log().all()
                .statusCode(200)
                .body("firstname", notNullValue());
    }

    @Test
    public void deveEfetuarReserva(){
        ReservaJson reserva = jsonReserva();

        //EFETUAR RESERVA
        given()
                .contentType("application/json")
                .body(reserva.json)
                .header("Cookie", "token=" + token)
                .log().all()
                .when()
                .post("/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("booking.firstname", equalTo(reserva.firstname))
                .body("booking.lastname", equalTo(reserva.lastname))
                .body("booking.totalprice", equalTo(reserva.totalprice))
                .body("booking.depositpaid", equalTo(reserva.depositpaid))
                .body("booking.bookingdates", notNullValue());
    }

    @Test
    public void deveAtualizarReserva(){
        ReservaJson reserva = jsonReserva();

        //EFETUAR RESERVA
        Integer idReserva =
                given()
                        .contentType("application/json")
                        .body(reserva.json)
                        .header("Cookie", "token=" + token)
                        .log().all()
                        .when()
                        .post("/booking")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .path("bookingid");

        //ATUALIZAR RESERVA
        String firstname = "Jim-Updated";
        String lastname = "Brown-Updated";
        Integer totalprice = 222;
        Boolean depositpaid = true;


        String jsonAtualizado = """
                    {
                        "firstname" : "%s",
                        "lastname" : "%s",
                        "totalprice" : %s,
                        "depositpaid" : %s,
                        "bookingdates" : {
                            "checkin" : "2018-01-01",
                            "checkout" : "2019-01-01"
                        },
                        "additionalneeds" : "Breakfast"
                    }
                """.formatted(firstname, lastname, totalprice, depositpaid);

        given()
                .contentType("application/json")
                .header("Cookie", "token=" + token)
                .body(jsonAtualizado)
                .log().all()
                .when()
                .put("/booking/" + idReserva)
                .then()
                .log().all()
                .statusCode(200)
                .body("firstname", equalTo(firstname))
                .body("lastname", equalTo(lastname))
                .body("totalprice", equalTo(totalprice))
                .body("depositpaid", equalTo(depositpaid))
                .body("bookingdates", notNullValue());

        //VERIFICAR RESERVA ATUALIZADA
        given()
                .accept("application/json")
                .header("Cookie", "token=" + token)
                .log().all()
                .when()
                .get("/booking/" + idReserva)
                .then()
                .log().all()
                .statusCode(200)
                .body("firstname", equalTo(firstname))
                .body("lastname", equalTo(lastname))
                .body("totalprice", equalTo(totalprice))
                .body("depositpaid", equalTo(depositpaid))
                .body("bookingdates", notNullValue());
    }

    @Test
    public void deveDeletarReservaPeloId(){
        ReservaJson reserva = jsonReserva();

        //EFETUAR RESERVA
        Integer idReserva =
        given()
                .contentType("application/json")
                .header("Cookie", "token=" + token)
                .body(reserva.json)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract()
                .path("bookingid");

        //DELETAR RESERVA
        given()
                .accept("application/json")
                .header("Cookie", "token=" + token)
                .log().all()
                .when()
                .delete("/booking/" + idReserva)
                .then()
                .log().all()
                .statusCode(20);    //BUG ENCONTRADO - RETORNAR STATUS CODE 200 MAS DEVERIA RETORNAR 201
    }
}
