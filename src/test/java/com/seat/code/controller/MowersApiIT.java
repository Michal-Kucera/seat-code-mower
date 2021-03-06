package com.seat.code.controller;

import static com.seat.code.controller.model.MowerInstruction.MOVE_FORWARD;
import static com.seat.code.controller.model.MowerInstruction.SPIN_LEFT;
import static com.seat.code.controller.model.MowerInstruction.SPIN_RIGHT;
import static com.seat.code.domain.entity.MowerEntityOrientation.EAST;
import static com.seat.code.domain.entity.MowerEntityOrientation.NORTH;
import static com.seat.code.util.TestControllerLayerObjectFactory.buildMower;
import static com.seat.code.util.TestDomainLayerObjectFactory.buildMowerEntity;
import static com.seat.code.util.TestDomainLayerObjectFactory.buildPlateauEntity;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.util.UriComponentsBuilder;

import com.seat.code.asserts.AssertMowerEntity;
import com.seat.code.controller.model.Mower;
import com.seat.code.controller.model.MowerInstruction;
import com.seat.code.controller.model.MowerOrientation;
import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.MowerEntityOrientation;
import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.domain.repository.MowerRepository;
import com.seat.code.domain.repository.PlateauRepository;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class MowersApiIT {

    private static final String CREATE_MOWER_RESOURCE_PATH = "/v1/plateaus/{plateauId}/mowers";
    private static final String GET_MOWER_RESOURCE_PATH = "/v1/plateaus/{plateauId}/mowers/{mowerId}";
    private static final String APPLY_MOWER_INSTRUCTIONS_RESOURCE_PATH = "/v1/plateaus/{plateauId}/mowers/{mowerId}/instructions";

    @LocalServerPort
    private int serverPort;

    @MockBean
    private PlateauRepository plateauRepository;

    @MockBean
    private MowerRepository mowerRepository;

    @Captor
    private ArgumentCaptor<MowerEntity> mowerEntityArgumentCaptor;

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

    @Test
    void createMower_shouldStoreMowerIntoDatabase_whenReceivedValidMowerRequestAndPlateauAlreadyExistsAndMowerPositionIsInRangeOfPlateau() {
        final Mower mowerRequest = buildMower();
        final PlateauEntity storedPlateauEntity = buildPlateauEntity();
        final MowerEntity storedMowerEntity = buildMowerEntity(storedPlateauEntity);

        when(plateauRepository.findById(storedPlateauEntity.getId())).thenReturn(Optional.of(storedPlateauEntity));
        when(mowerRepository.save(any(MowerEntity.class))).thenReturn(storedMowerEntity);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(mowerRequest)
            .and().log().all()
            .when()
            .post(CREATE_MOWER_RESOURCE_PATH, storedPlateauEntity.getId())
            .then()
            .log().all()
            .and().statusCode(CREATED.value());

        verify(plateauRepository).findById(storedPlateauEntity.getId());
        verify(mowerRepository).save(mowerEntityArgumentCaptor.capture());
        AssertMowerEntity.assertThat(mowerEntityArgumentCaptor)
            .isNotNull()
            .hasNullId()
            .hasNullVersion()
            .hasName(mowerRequest.getName())
            .hasPlateau(storedPlateauEntity)
            .hasLatitude(mowerRequest.getPosition().getLatitude())
            .hasLongitude(mowerRequest.getPosition().getLongitude())
            .hasOrientation(MowerEntityOrientation.valueOf(mowerRequest.getPosition().getOrientation().name()));
        verifyNoMoreInteractions(plateauRepository, mowerRepository);
    }

    @Test
    void createMower_shouldReturn201WithLocationHeader_whenReceivedValidMowerRequestAndPlateauAlreadyExistsAndMowerWasCreated() {
        final Mower mowerRequest = buildMower();
        final PlateauEntity storedPlateauEntity = buildPlateauEntity();
        final MowerEntity storedMowerEntity = buildMowerEntity(storedPlateauEntity);
        final String expectedLocationHeaderValue = UriComponentsBuilder.fromUriString(baseURI + ":" + serverPort + GET_MOWER_RESOURCE_PATH)
            .build(storedPlateauEntity.getId(), storedMowerEntity.getId())
            .toString();

        when(plateauRepository.findById(storedPlateauEntity.getId())).thenReturn(Optional.of(storedPlateauEntity));
        when(mowerRepository.save(any(MowerEntity.class))).thenReturn(storedMowerEntity);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(mowerRequest)
            .and().log().all()
            .when()
            .post(CREATE_MOWER_RESOURCE_PATH, storedPlateauEntity.getId())
            .then()
            .log().all()
            .and().statusCode(CREATED.value())
            .and().header(LOCATION, expectedLocationHeaderValue);

        verify(plateauRepository).findById(storedPlateauEntity.getId());
        verify(mowerRepository).save(any(MowerEntity.class));
        verifyNoMoreInteractions(plateauRepository, mowerRepository);
    }

    @Test
    void createMower_shouldNotStoreMowerAdReturnBadRequest_whenReceivedValidMowerRequestAndPlateauNotFoundInDatabase() {
        final UUID plateauId = UUID.randomUUID();
        final Mower mowerRequest = buildMower();

        when(plateauRepository.findById(plateauId)).thenReturn(Optional.empty());

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(mowerRequest)
            .and().log().all()
            .when()
            .post(CREATE_MOWER_RESOURCE_PATH, plateauId)
            .then()
            .log().all()
            .and().statusCode(BAD_REQUEST.value());

        verify(plateauRepository).findById(plateauId);
        verifyNoMoreInteractions(plateauRepository);
        verifyNoInteractions(mowerRepository);
    }

    @Test
    void createMower_shouldNotStoreMowerAndReturnBadRequest_whenReceivedValidMowerRequestAndPlateauExistsInDatabaseAndMowerHasLatitudePositionBiggerThenPlateauLength() {
        final PlateauEntity storedPlateauEntity = buildPlateauEntity();
        final Mower mowerRequest = buildMower();
        mowerRequest.getPosition().setLatitude(storedPlateauEntity.getLength() + 1);
        mowerRequest.getPosition().setLongitude(storedPlateauEntity.getWidth());

        when(plateauRepository.findById(storedPlateauEntity.getId())).thenReturn(Optional.of(storedPlateauEntity));

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(mowerRequest)
            .and().log().all()
            .when()
            .post(CREATE_MOWER_RESOURCE_PATH, storedPlateauEntity.getId())
            .then()
            .log().all()
            .and().statusCode(BAD_REQUEST.value());

        verify(plateauRepository).findById(storedPlateauEntity.getId());
        verifyNoMoreInteractions(plateauRepository);
        verifyNoInteractions(mowerRepository);
    }

    @Test
    void createMower_shouldNotStoreMowerAndReturnBadRequest_whenReceivedValidMowerRequestAndPlateauExistsInDatabaseAndMowerHasLongitudePositionBiggerThenPlateauWidth() {
        final PlateauEntity storedPlateauEntity = buildPlateauEntity();
        final Mower mowerRequest = buildMower();
        mowerRequest.getPosition().setLatitude(storedPlateauEntity.getLength());
        mowerRequest.getPosition().setLongitude(storedPlateauEntity.getWidth() + 1);

        when(plateauRepository.findById(storedPlateauEntity.getId())).thenReturn(Optional.of(storedPlateauEntity));

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(mowerRequest)
            .and().log().all()
            .when()
            .post(CREATE_MOWER_RESOURCE_PATH, storedPlateauEntity.getId())
            .then()
            .log().all()
            .and().statusCode(BAD_REQUEST.value());

        verify(plateauRepository).findById(storedPlateauEntity.getId());
        verifyNoMoreInteractions(plateauRepository);
        verifyNoInteractions(mowerRepository);
    }

    @Test
    void createMower_shouldNotStoreMowerAndReturnBadRequest_whenReceivedValidMowerRequestAndPlateauExistsInDatabaseAndMowerHasSamePositionAsAlreadyExistinMower() {
        final PlateauEntity storedPlateauEntity = buildPlateauEntity();
        storedPlateauEntity.getMowers().clear();
        final MowerEntity alreadyExistingMowerEntity = buildMowerEntity(storedPlateauEntity);
        storedPlateauEntity.getMowers().add(alreadyExistingMowerEntity);
        final Mower mowerRequest = buildMower();
        alreadyExistingMowerEntity.setLatitude(mowerRequest.getPosition().getLatitude());
        alreadyExistingMowerEntity.setLongitude(mowerRequest.getPosition().getLongitude());

        when(plateauRepository.findById(storedPlateauEntity.getId())).thenReturn(Optional.of(storedPlateauEntity));

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .and().body(mowerRequest)
            .and().log().all()
            .when()
            .post(CREATE_MOWER_RESOURCE_PATH, storedPlateauEntity.getId())
            .then()
            .log().all()
            .and().statusCode(BAD_REQUEST.value());

        verify(plateauRepository).findById(storedPlateauEntity.getId());
        verifyNoMoreInteractions(plateauRepository);
        verifyNoInteractions(mowerRepository);
    }

    @Test
    void getMower_shouldReturnMowerWith200HttpCode_whenRequestedMowerRelatedToRequestedPlateauFoundInDatabase() {
        final MowerEntity storedMowerEntity = buildMowerEntity();

        when(mowerRepository.findOneByIdAndPlateauId(storedMowerEntity.getId(), storedMowerEntity.getPlateau().getId())).thenReturn(Optional.of(storedMowerEntity));

        given()
            .log().all()
            .and().accept(APPLICATION_JSON_VALUE)
            .when()
            .get(GET_MOWER_RESOURCE_PATH, storedMowerEntity.getPlateau().getId(), storedMowerEntity.getId())
            .then()
            .log().all()
            .and().statusCode(OK.value())
            .and().contentType(APPLICATION_JSON_VALUE)
            .and().body("name", equalTo(storedMowerEntity.getName()))
            .and().body("position.latitude", equalTo(storedMowerEntity.getLatitude()))
            .and().body("position.longitude", equalTo(storedMowerEntity.getLongitude()))
            .and().body("position.orientation", equalTo(MowerOrientation.valueOf(storedMowerEntity.getOrientation().name()).getValue()));

        verify(mowerRepository).findOneByIdAndPlateauId(storedMowerEntity.getId(), storedMowerEntity.getPlateau().getId());
        verifyNoMoreInteractions(mowerRepository);
    }

    @Test
    void getMower_shouldReturnBadRequest_whenRequestedMowerRelatedToRequestedPlateauNotFoundInDatabase() {
        final UUID plateauId = UUID.randomUUID();
        final UUID mowerId = UUID.randomUUID();

        when(mowerRepository.findOneByIdAndPlateauId(mowerId, plateauId)).thenReturn(Optional.empty());

        given()
            .log().all()
            .and().accept(APPLICATION_JSON_VALUE)
            .when()
            .get(GET_MOWER_RESOURCE_PATH, plateauId, mowerId)
            .then()
            .log().all()
            .and().statusCode(BAD_REQUEST.value());

        verify(mowerRepository).findOneByIdAndPlateauId(mowerId, plateauId);
        verifyNoMoreInteractions(mowerRepository);
    }

    @ParameterizedTest
    @MethodSource("getPlateauAndMowerDetailsForApplyMowerInstructionsTest")
    void applyMowerInstructions_shouldApplyInstructionsToTargetingMower_whenMowerExistsAndIsRelatedToTargetingPlateauAndInstructionsAreNotGettingOutOfTheRangeOfPlateauGrid(final Integer plateauLength,
                                                                                                                                                                            final Integer plateauWidth,
                                                                                                                                                                            final Integer startMowerLongitude,
                                                                                                                                                                            final Integer startMowerLatitude,
                                                                                                                                                                            final MowerEntityOrientation startMowerOrientation,
                                                                                                                                                                            final int finalMowerLongitude,
                                                                                                                                                                            final int finalMowerLatitude,
                                                                                                                                                                            final MowerEntityOrientation finalMowerOrientation,
                                                                                                                                                                            final List<MowerInstruction> mowerInstructions) {
        final MowerEntity storedMowerEntity = buildMowerEntity();
        storedMowerEntity.setLatitude(startMowerLatitude);
        storedMowerEntity.setLongitude(startMowerLongitude);
        storedMowerEntity.setOrientation(startMowerOrientation);
        storedMowerEntity.getPlateau().setLength(plateauLength);
        storedMowerEntity.getPlateau().setWidth(plateauWidth);
        final UUID mowerId = storedMowerEntity.getId();
        final UUID plateauId = storedMowerEntity.getPlateau().getId();

        when(mowerRepository.findOneByIdAndPlateauId(mowerId, plateauId)).thenReturn(Optional.of(storedMowerEntity));

        given()
            .log().all()
            .and().body(mowerInstructions)
            .and().contentType(APPLICATION_JSON_VALUE)
            .when()
            .post(APPLY_MOWER_INSTRUCTIONS_RESOURCE_PATH, plateauId, mowerId)
            .then()
            .log().all()
            .and().statusCode(NO_CONTENT.value());

        verify(mowerRepository).findOneByIdAndPlateauId(mowerId, plateauId);
        verify(mowerRepository).save(mowerEntityArgumentCaptor.capture());
        AssertMowerEntity.assertThat(mowerEntityArgumentCaptor)
            .isNotNull()
            .hasId(mowerId)
            .hasVersion(storedMowerEntity.getVersion())
            .hasName(storedMowerEntity.getName())
            .hasPlateau(storedMowerEntity.getPlateau())
            .hasLatitude(finalMowerLatitude)
            .hasLongitude(finalMowerLongitude)
            .hasOrientation(finalMowerOrientation);
        verifyNoMoreInteractions(mowerRepository);
    }

    private static Stream<Arguments> getPlateauAndMowerDetailsForApplyMowerInstructionsTest() {
        final Arguments firstTestCaseScenarioArgument = buildPlateauAndMowerDetailsArgument(5, 5, 1, 2, NORTH, 1, 3, NORTH, asList(SPIN_LEFT,
            MOVE_FORWARD,
            SPIN_LEFT,
            MOVE_FORWARD,
            SPIN_LEFT,
            MOVE_FORWARD,
            SPIN_LEFT,
            MOVE_FORWARD,
            MOVE_FORWARD));
        final Arguments secondTestCaseScenarioArgument = buildPlateauAndMowerDetailsArgument(5, 5, 3, 3, EAST, 5, 1, EAST, asList(MOVE_FORWARD,
            MOVE_FORWARD,
            SPIN_RIGHT,
            MOVE_FORWARD,
            MOVE_FORWARD,
            SPIN_RIGHT,
            MOVE_FORWARD,
            SPIN_RIGHT,
            SPIN_RIGHT,
            MOVE_FORWARD));
        return Stream.of(firstTestCaseScenarioArgument, secondTestCaseScenarioArgument);
    }

    private static Arguments buildPlateauAndMowerDetailsArgument(final Integer plateauLength,
                                                                 final Integer plateauWidth,
                                                                 final Integer startMowerLongitude,
                                                                 final Integer startMowerLatitude,
                                                                 final MowerEntityOrientation startMowerOrientation,
                                                                 final int finalMowerLongitude,
                                                                 final int finalMowerLatitude,
                                                                 final MowerEntityOrientation finalMowerOrientation,
                                                                 final List<MowerInstruction> mowerInstructions) {
        return Arguments.of(plateauLength,
            plateauWidth,
            startMowerLongitude,
            startMowerLatitude,
            startMowerOrientation,
            finalMowerLongitude,
            finalMowerLatitude,
            finalMowerOrientation,
            mowerInstructions);
    }
}
