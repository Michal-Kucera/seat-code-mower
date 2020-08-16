package com.seat.code.controller;

import static com.seat.code.util.TestControllerLayerObjectFactory.buildRectangularPlateau;
import static com.seat.code.util.TestDomainLayerObjectFactory.buildPlateauEntity;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.util.UriComponentsBuilder;

import com.seat.code.asserts.AssertPlateauEntity;
import com.seat.code.controller.model.RectangularPlateau;
import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.domain.repository.PlateauRepository;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class PlateausApiIT {

    private static final String CREATE_PLATEAU_RESOURCE_PATH = "/v1/plateaus";
    private static final String GET_PLATEAU_RESOURCE_PATH = "/v1/plateaus/{plateauId}";

    @LocalServerPort
    private int serverPort;

    @MockBean
    private PlateauRepository plateauRepository;

    @Captor
    private ArgumentCaptor<PlateauEntity> plateauEntityArgumentCaptor;

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
        final PlateauEntity storedPlateauEntity = buildPlateauEntity();

        when(plateauRepository.save(any(PlateauEntity.class))).thenReturn(storedPlateauEntity);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(createPlateauRequest)
            .and().log().all()
            .when()
            .post(CREATE_PLATEAU_RESOURCE_PATH)
            .then()
            .log().all()
            .and().statusCode(CREATED.value());

        verify(plateauRepository).save(plateauEntityArgumentCaptor.capture());
        AssertPlateauEntity.assertThat(plateauEntityArgumentCaptor)
            .isNotNull()
            .hasNullId()
            .hasName(createPlateauRequest.getName())
            .hasLength(createPlateauRequest.getSize().getLength())
            .hasWidth(createPlateauRequest.getSize().getWidth())
            .hasNoMowers();
        verifyNoMoreInteractions(plateauRepository);
    }

    @Test
    void createPlateau_shouldReturn201WithLocationHeader_whenReceivedValidPlateauRequestAndPlateauWasCreated() {
        final RectangularPlateau createPlateauRequest = buildRectangularPlateau();
        final PlateauEntity storedPlateauEntity = buildPlateauEntity();
        final String expectedLocationHeaderValue = UriComponentsBuilder.fromUriString(baseURI + ":" + serverPort + GET_PLATEAU_RESOURCE_PATH)
            .build(storedPlateauEntity.getId())
            .toString();

        when(plateauRepository.save(any(PlateauEntity.class))).thenReturn(storedPlateauEntity);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(createPlateauRequest)
            .and().log().all()
            .when()
            .post(CREATE_PLATEAU_RESOURCE_PATH)
            .then()
            .log().all()
            .and().statusCode(CREATED.value())
            .and().header(LOCATION, expectedLocationHeaderValue);
        verify(plateauRepository).save(any(PlateauEntity.class));
        verifyNoMoreInteractions(plateauRepository);
    }

    @Test
    void getPlateau_shouldReturnPlateauWith200HttpCode_whenRequestedPlateauFoundInDatabase() {
        final PlateauEntity storedPlateauEntity = buildPlateauEntity();

        when(plateauRepository.findById(storedPlateauEntity.getId())).thenReturn(Optional.ofNullable(storedPlateauEntity));

        given()
            .log().all()
            .and().accept(APPLICATION_JSON_VALUE)
            .when()
            .get(GET_PLATEAU_RESOURCE_PATH, storedPlateauEntity.getId())
            .then()
            .log().all()
            .and().statusCode(OK.value())
            .and().contentType(APPLICATION_JSON_VALUE)
            .and().body("name", equalTo(storedPlateauEntity.getName()))
            .and().body("size.length", equalTo(storedPlateauEntity.getLength()))
            .and().body("size.width", equalTo(storedPlateauEntity.getWidth()))
            .and().body("mowers", hasSize(4))
            .and().body("mowers", containsInAnyOrder(storedPlateauEntity.getMowers().get(0).getId().toString(),
            storedPlateauEntity.getMowers().get(1).getId().toString(),
            storedPlateauEntity.getMowers().get(2).getId().toString(),
            storedPlateauEntity.getMowers().get(3).getId().toString()));
        verify(plateauRepository).findById(storedPlateauEntity.getId());
        verifyNoMoreInteractions(plateauRepository);
    }

    @Test
    void getPlateau_shouldReturnBadRequest_whenRequestedPlateauNotFoundInDatabase() {
        final UUID plateauId = UUID.randomUUID();

        when(plateauRepository.findById(plateauId)).thenReturn(Optional.empty());

        given()
            .log().all()
            .and().accept(APPLICATION_JSON_VALUE)
            .when()
            .get(GET_PLATEAU_RESOURCE_PATH, plateauId)
            .then()
            .log().all()
            .and().statusCode(BAD_REQUEST.value());
        verify(plateauRepository).findById(plateauId);
        verifyNoMoreInteractions(plateauRepository);
    }
}
