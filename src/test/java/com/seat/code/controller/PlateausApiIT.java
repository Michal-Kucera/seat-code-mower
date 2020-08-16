package com.seat.code.controller;

import static com.seat.code.util.TestControllerLayerObjectFactory.buildRectangularPlateau;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.seat.code.asserts.AssertPlateauEntity;
import com.seat.code.controller.model.RectangularPlateau;
import com.seat.code.domain.entity.BaseEntity;
import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.domain.repository.PlateauRepository;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
@AutoConfigureTestDatabase(replace = Replace.ANY)
class PlateausApiIT {

    private static final String CREATE_PLATEAU_RESOURCE_PATH = "/v1/plateaus";
    private static final String GET_PLATEAU_RESOURCE_PATH = "/v1/plateaus/{plateauId}";

    @LocalServerPort
    private int serverPort;

    @Autowired
    private PlateauRepository plateauRepository;

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

    @Test
    void createPlateau_shouldStorePlateauIntoDatabase_whenReceivedValidPlateauRequest() {
        final RectangularPlateau createPlateauRequest = buildRectangularPlateau();

        plateauRepository.deleteAll();

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(createPlateauRequest)
            .and().log().all()
            .when()
            .post(CREATE_PLATEAU_RESOURCE_PATH)
            .then()
            .log().all()
            .and().statusCode(CREATED.value());

        final List<PlateauEntity> plateaus = plateauRepository.findAll();
        assertThat(plateaus).hasSize(1);
        AssertPlateauEntity.assertThat(plateaus.get(0))
            .isNotNull()
            .hasNotNullId()
            .hasNotNullCreatedDateTime()
            .hasName(createPlateauRequest.getName())
            .hasLength(createPlateauRequest.getSize().getLength())
            .hasWidth(createPlateauRequest.getSize().getWidth())
            .hasNoMowers();
    }

    @Test
    void createPlateau_shouldReturn201WithLocationHeader_whenReceivedValidPlateauRequestAndPlateauWasCreated() {
        final RectangularPlateau createPlateauRequest = buildRectangularPlateau();

        plateauRepository.deleteAll();

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(createPlateauRequest)
            .and().log().all()
            .when()
            .post(CREATE_PLATEAU_RESOURCE_PATH)
            .then()
            .log().all()
            .and().statusCode(CREATED.value())
            .and().header(LOCATION, getPlateauLocationHeaderValue());
    }

    private String getPlateauLocationHeaderValue() {
        final List<PlateauEntity> plateaus = plateauRepository.findAll();
        assertThat(plateaus).hasSize(1);
        final UUID plateauId = plateaus.stream()
            .findFirst()
            .map(BaseEntity::getId)
            .orElse(null);
        assertThat(plateauId).isNotNull();
        return UriComponentsBuilder.fromUriString(baseURI + ":" + serverPort + GET_PLATEAU_RESOURCE_PATH)
            .build(plateauId)
            .toString();
    }
}
