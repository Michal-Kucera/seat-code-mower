package com.seat.code.controller;

import static com.seat.code.util.TestControllerLayerObjectFactory.buildMower;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import com.seat.code.controller.model.Mower;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class MowersApiIT {

    private static final String CREATE_MOWER_RESOURCE_PATH = "/v1/plateaus/{plateauId}/mowers";
    private static final String GET_MOWER_RESOURCE_PATH = "/v1/plateaus/{plateauId}/mowers/{mowerId}";
    private static final String APPLY_MOWER_INSTRUCTIONS_RESOURCE_PATH = "/v1/plateaus/{plateauId}/mowers/{mowerId}/instructions";

    @LocalServerPort
    private int serverPort;

    @BeforeEach
    void setUp() {
        port = serverPort;
    }

    @Test
    void createMower_shouldReturn400_whenReceivedMowerRequestHasNoNameProvided() {
        final Mower createMowerRequest = buildMower();
        createMowerRequest.setName(null);

        final UUID plateauId = UUID.randomUUID();

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(createMowerRequest)
            .and().log().all()
            .when()
            .post(CREATE_MOWER_RESOURCE_PATH, plateauId)
            .then()
            .log().all()
            .and().statusCode(BAD_REQUEST.value());
    }

    @Test
    void createMower_shouldReturn400_whenReceivedMowerRequestHasNoPositionProvided() {
        final Mower createMowerRequest = buildMower();
        createMowerRequest.setPosition(null);

        final UUID plateauId = UUID.randomUUID();

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(createMowerRequest)
            .and().log().all()
            .when()
            .post(CREATE_MOWER_RESOURCE_PATH, plateauId)
            .then()
            .log().all()
            .and().statusCode(BAD_REQUEST.value());
    }

    @Test
    void createMower_shouldReturn400_whenReceivedMowerRequestHasPositionLatitudeLessThan1() {
        final Mower createMowerRequest = buildMower();
        createMowerRequest.getPosition().setLatitude(0);

        final UUID plateauId = UUID.randomUUID();

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(createMowerRequest)
            .and().log().all()
            .when()
            .post(CREATE_MOWER_RESOURCE_PATH, plateauId)
            .then()
            .log().all()
            .and().statusCode(BAD_REQUEST.value());
    }

    @Test
    void createMower_shouldReturn400_whenReceivedMowerRequestHasPositionLongitudeLessThan1() {
        final Mower createMowerRequest = buildMower();
        createMowerRequest.getPosition().setLongitude(0);

        final UUID plateauId = UUID.randomUUID();

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(createMowerRequest)
            .and().log().all()
            .when()
            .post(CREATE_MOWER_RESOURCE_PATH, plateauId)
            .then()
            .log().all()
            .and().statusCode(BAD_REQUEST.value());
    }

    @Test
    void createMower_shouldReturn400_whenReceivedMowerRequestHasNoPositionOrientationProvided() {
        final Mower createMowerRequest = buildMower();
        createMowerRequest.getPosition().setOrientation(null);

        final UUID plateauId = UUID.randomUUID();

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(createMowerRequest)
            .and().log().all()
            .when()
            .post(CREATE_MOWER_RESOURCE_PATH, plateauId)
            .then()
            .log().all()
            .and().statusCode(BAD_REQUEST.value());
    }
}
