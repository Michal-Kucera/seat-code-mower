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
import com.seat.code.service.mower.model.MowerInstruction;

@ExtendWith(MockitoExtension.class)
class MowerSpinLeftInstructionProcessorTest {

    @InjectMocks
    private MowerSpinLeftInstructionProcessor underTest;

    @Test
    void isInstructionSupported_shouldReturnTrueForSpinLeft() {
        assertThat(underTest.isInstructionSupported(SPIN_LEFT)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("getInstructionsForNotSupportedInstructionsTest")
    void isInstructionSupported_shouldReturnFalseForNotSupportedInstructions(final MowerInstruction mowerInstruction) {
        assertThat(underTest.isInstructionSupported(mowerInstruction)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("getNewOrientationsForApplyInstructionTest")
    void applyInstruction_shouldApplyNewOrientationToMower(final MowerEntityOrientation oldOrientation,
                                                           final MowerEntityOrientation newOrientation) {
        final MowerEntity mower = buildMowerEntity();
        mower.setOrientation(oldOrientation);

        underTest.applyInstruction(mower);

        assertThat(mower.getOrientation()).isEqualTo(newOrientation);
    }

    @ParameterizedTest
    @MethodSource("getNewOrientationsForApplyInstructionTest")
    void applyInstruction_shouldNotChangeMowerLatitudeNorLongitude_whenNewOrientationIsAppliedToMower(final MowerEntityOrientation oldOrientation,
                                                                                                      final MowerEntityOrientation newOrientation) {
        final MowerEntity mower = buildMowerEntity();
        mower.setOrientation(oldOrientation);
        final Integer originalLatitude = mower.getLatitude();
        final Integer originalLongitude = mower.getLongitude();

        underTest.applyInstruction(mower);

        AssertMowerEntity.assertThat(mower)
            .isNotNull()
            .hasLatitude(originalLatitude)
            .hasLongitude(originalLongitude);
    }

    private static Stream<Arguments> getInstructionsForNotSupportedInstructionsTest() {
        return Stream.of(Arguments.of(SPIN_RIGHT), Arguments.of(MOVE_FORWARD));
    }

    private static Stream<Arguments> getNewOrientationsForApplyInstructionTest() {
        return Stream.of(buildNewOrientationsArgument(NORTH, WEST),
            buildNewOrientationsArgument(EAST, NORTH),
            buildNewOrientationsArgument(SOUTH, EAST),
            buildNewOrientationsArgument(WEST, SOUTH));
    }

    private static Arguments buildNewOrientationsArgument(final MowerEntityOrientation oldOrientation,
                                                          final MowerEntityOrientation newOrientation) {
        return Arguments.of(oldOrientation, newOrientation);
    }
}
