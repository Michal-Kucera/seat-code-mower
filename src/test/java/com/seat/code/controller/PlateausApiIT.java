package com.seat.code.controller;

import static com.seat.code.util.TestControllerLayerObjectFactory.buildRectangularPlateau;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import com.seat.code.controller.model.RectangularPlateau;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class PlateausApiIT {

    private static final String CREATE_PLATEAU_RESOURCE_PATH = "/v1/plateaus";

    @LocalServerPort
    private int serverPort;

    @BeforeEach
    void setUp() {
        port = serverPort;
    }

    @Test
    void createPlateau_shouldReturn400_whenReceivedPlateauRequestHasNoNameProvided() {
        final RectangularPlateau createPlateauRequest = buildRectangularPlateau();
        createPlateauRequest.setName(null);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(createPlateauRequest)
            .and().log().all()
            .when()
            .post(CREATE_PLATEAU_RESOURCE_PATH)
            .then()
            .log().all()
            .and().statusCode(BAD_REQUEST.value());
    }

    @Test
    void createPlateau_shouldReturn400_whenReceivedPlateauRequestHasNoSizeProvided() {
        final RectangularPlateau createPlateauRequest = buildRectangularPlateau();
        createPlateauRequest.setSize(null);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(createPlateauRequest)
            .and().log().all()
            .when()
            .post(CREATE_PLATEAU_RESOURCE_PATH)
            .then()
            .log().all()
            .and().statusCode(BAD_REQUEST.value());
    }

    @Test
    void createPlateau_shouldReturn400_whenReceivedPlateauRequestHasSizeLengthLessThan1() {
        final RectangularPlateau createPlateauRequest = buildRectangularPlateau();
        createPlateauRequest.getSize().setLength(0);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(createPlateauRequest)
            .and().log().all()
            .when()
            .post(CREATE_PLATEAU_RESOURCE_PATH)
            .then()
            .log().all()
            .and().statusCode(BAD_REQUEST.value());
    }

    @Test
    void createPlateau_shouldReturn400_whenReceivedPlateauRequestHasSizeWidthLessThan1() {
        final RectangularPlateau createPlateauRequest = buildRectangularPlateau();
        createPlateauRequest.getSize().setWidth(0);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(createPlateauRequest)
            .and().log().all()
            .when()
            .post(CREATE_PLATEAU_RESOURCE_PATH)
            .then()
            .log().all()
            .and().statusCode(BAD_REQUEST.value());
    }
}
