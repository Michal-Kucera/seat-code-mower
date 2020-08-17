package com.seat.code.service.mower.instruction.processor;

import static com.seat.code.domain.entity.MowerEntityOrientation.EAST;
import static com.seat.code.domain.entity.MowerEntityOrientation.NORTH;
import static com.seat.code.domain.entity.MowerEntityOrientation.SOUTH;
import static com.seat.code.domain.entity.MowerEntityOrientation.WEST;
import static com.seat.code.service.mower.model.MowerInstruction.MOVE_FORWARD;
import static com.seat.code.service.mower.model.MowerInstruction.SPIN_LEFT;
import static com.seat.code.service.mower.model.MowerInstruction.SPIN_RIGHT;
import static com.seat.code.util.TestDomainLayerObjectFactory.buildMowerEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.asserts.AssertMowerEntity;
import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.MowerEntityOrientation;
import com.seat.code.service.mower.exception.MowerPositionAlreadyTakenException;
import com.seat.code.service.mower.exception.MowerPositionOutOfRangeException;
import com.seat.code.service.mower.model.MowerInstruction;

@ExtendWith(MockitoExtension.class)
class MowerMoveForwardInstructionProcessorTest {

    private static final int MINIMUM_LONGITUDE = 0;
    private static final int MINIMUM_LATITUDE = 0;

    @InjectMocks
    private MowerMoveForwardInstructionProcessor underTest;

    @Test
    void isInstructionSupported_shouldReturnTrueForMoveForward() {
        assertThat(underTest.isInstructionSupported(MOVE_FORWARD)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("getInstructionsForNotSupportedInstructionsTest")
    void isInstructionSupported_shouldReturnFalseForNotSupportedInstructions(final MowerInstruction mowerInstruction) {
        assertThat(underTest.isInstructionSupported(mowerInstruction)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("getPlateauAndMowerDetailsForApplyNewMowerLatitudeAndLongitudeTest")
    void applyInstruction_shouldApplyNewLatitudeAndLongitudeToMower_whenNewPositionIsInPlateauRangeAndMowerTargetingPositionIsNotTakenInPlateauByAnotherMower(final Integer plateauLength,
                                                                                                                                                              final Integer plateauWidth,
                                                                                                                                                              final Integer startLongitude,
                                                                                                                                                              final Integer startLatitude,
                                                                                                                                                              final MowerEntityOrientation mowerOrientation,
                                                                                                                                                              final Integer expectedLongitude,
                                                                                                                                                              final Integer expectedLatitude) {
        final MowerEntity mower = buildMowerEntity();
        mower.getPlateau().setLength(plateauLength);
        mower.getPlateau().setWidth(plateauWidth);
        mower.setLatitude(startLatitude);
        mower.setLongitude(startLongitude);
        mower.setOrientation(mowerOrientation);

        underTest.applyInstruction(mower);

        AssertMowerEntity.assertThat(mower)
            .isNotNull()
            .hasLatitude(expectedLatitude)
            .hasLongitude(expectedLongitude);
    }

    @ParameterizedTest
    @MethodSource("getPlateauAndMowerDetailsForApplyNewMowerLatitudeAndLongitudeTest")
    void applyInstruction_shouldNotChangeOrientationOfMower_whenNewPositionIsInPlateauRangeAndMowerTargetingPositionIsNotTakenInPlateauByAnotherMower(final Integer plateauLength,
                                                                                                                                                      final Integer plateauWidth,
                                                                                                                                                      final Integer startLongitude,
                                                                                                                                                      final Integer startLatitude,
                                                                                                                                                      final MowerEntityOrientation mowerOrientation,
                                                                                                                                                      final Integer expectedLongitude,
                                                                                                                                                      final Integer expectedLatitude) {
        final MowerEntity mower = buildMowerEntity();
        mower.getPlateau().setLength(plateauLength);
        mower.getPlateau().setWidth(plateauWidth);
        mower.setLatitude(startLatitude);
        mower.setLongitude(startLongitude);
        mower.setOrientation(mowerOrientation);

        underTest.applyInstruction(mower);

        AssertMowerEntity.assertThat(mower)
            .isNotNull()
            .hasOrientation(mowerOrientation);
    }

    @ParameterizedTest
    @MethodSource("getPlateauAndMowerDetailsForApplyNewMowerOutsideOfRangeOfPlateauTest")
    void applyInstruction_shouldThrowMowerPositionOutOfRangeException_whenNewPositionIsNotInPlateauRange(final Integer plateauLength,
                                                                                                         final Integer plateauWidth,
                                                                                                         final Integer startLongitude,
                                                                                                         final Integer startLatitude,
                                                                                                         final MowerEntityOrientation mowerOrientation) {
        final MowerEntity mower = buildMowerEntity();
        mower.getPlateau().setLength(plateauLength);
        mower.getPlateau().setWidth(plateauWidth);
        mower.setLatitude(startLatitude);
        mower.setLongitude(startLongitude);
        mower.setOrientation(mowerOrientation);

        assertThatThrownBy(() -> underTest.applyInstruction(mower))
            .isInstanceOf(MowerPositionOutOfRangeException.class)
            .hasMessage("Mower instruction to move forward cannot be applied because mower would go out of the range of plateau");
    }

    @Test
    void applyInstruction_shouldThrowMowerPositionAlreadyTakenException_whenNewPositionIsAlreadyTakenByAnotherMower() {
        final MowerEntity mower = buildMowerEntity();
        mower.setLatitude(1);
        mower.setLongitude(2);
        mower.setOrientation(NORTH);
        mower.getPlateau().getMowers().clear();
        final MowerEntity anotherMower = buildMowerEntity();
        mower.getPlateau().getMowers().add(anotherMower);
        anotherMower.setLatitude(2);
        anotherMower.setLongitude(2);

        assertThatThrownBy(() -> underTest.applyInstruction(mower))
            .isInstanceOf(MowerPositionAlreadyTakenException.class)
            .hasMessage("Mower's targeting position is already taken by another mower in plateau");
    }

    private static Stream<Arguments> getInstructionsForNotSupportedInstructionsTest() {
        return Stream.of(Arguments.of(SPIN_LEFT), Arguments.of(SPIN_RIGHT));
    }

    private static Stream<Arguments> getPlateauAndMowerDetailsForApplyNewMowerLatitudeAndLongitudeTest() {
        final Arguments northScenarioArgument = buildPlateauAndMowerDetailsForApplyNewMowerLatitudeAndLongitudeTest(5, 5, 2, 4, NORTH, 2, 5);
        final Arguments westScenarioArgument = buildPlateauAndMowerDetailsForApplyNewMowerLatitudeAndLongitudeTest(5, 5, 4, 2, WEST, 3, 2);
        final Arguments eastScenarioArgument = buildPlateauAndMowerDetailsForApplyNewMowerLatitudeAndLongitudeTest(5, 5, 2, 4, EAST, 3, 4);
        final Arguments southScenarioArgument = buildPlateauAndMowerDetailsForApplyNewMowerLatitudeAndLongitudeTest(5, 5, 4, 2, SOUTH, 4, 1);
        return Stream.of(northScenarioArgument, westScenarioArgument, eastScenarioArgument, southScenarioArgument);
    }

    private static Stream<Arguments> getPlateauAndMowerDetailsForApplyNewMowerOutsideOfRangeOfPlateauTest() {
        final Arguments northScenarioArgument = buildPlateauAndMowerDetailsForApplyNewMowerOutsideOfRangeOfPlateauTest(5, 5, 2, 5, NORTH);
        final Arguments westScenarioArgument = buildPlateauAndMowerDetailsForApplyNewMowerOutsideOfRangeOfPlateauTest(5, 5, MINIMUM_LONGITUDE, 4, WEST);
        final Arguments eastScenarioArgument = buildPlateauAndMowerDetailsForApplyNewMowerOutsideOfRangeOfPlateauTest(5, 5, 5, 3, EAST);
        final Arguments southScenarioArgument = buildPlateauAndMowerDetailsForApplyNewMowerOutsideOfRangeOfPlateauTest(5, 5, 2, MINIMUM_LATITUDE, SOUTH);
        return Stream.of(northScenarioArgument, westScenarioArgument, eastScenarioArgument, southScenarioArgument);
    }

    private static Arguments buildPlateauAndMowerDetailsForApplyNewMowerLatitudeAndLongitudeTest(final Integer plateauLength,
                                                                                                 final Integer plateauWidth,
                                                                                                 final Integer startLongitude,
                                                                                                 final Integer startLatitude,
                                                                                                 final MowerEntityOrientation mowerOrientation,
                                                                                                 final Integer expectedLongitude,
                                                                                                 final Integer expectedLatitude) {
        return Arguments.of(plateauLength,
            plateauWidth,
            startLongitude,
            startLatitude,
            mowerOrientation,
            expectedLongitude,
            expectedLatitude);
    }

    private static Arguments buildPlateauAndMowerDetailsForApplyNewMowerOutsideOfRangeOfPlateauTest(final Integer plateauLength,
                                                                                                    final Integer plateauWidth,
                                                                                                    final Integer startLongitude,
                                                                                                    final Integer startLatitude,
                                                                                                    final MowerEntityOrientation mowerOrientation) {
        return Arguments.of(plateauLength,
            plateauWidth,
            startLongitude,
            startLatitude,
            mowerOrientation);
    }
}
