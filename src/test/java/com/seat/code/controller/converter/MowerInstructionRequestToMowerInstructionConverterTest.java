package com.seat.code.controller.converter;

import static com.seat.code.controller.model.MowerInstruction.MOVE_FORWARD;
import static com.seat.code.controller.model.MowerInstruction.SPIN_LEFT;
import static com.seat.code.controller.model.MowerInstruction.SPIN_RIGHT;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.controller.model.MowerInstruction;

@ExtendWith(MockitoExtension.class)
class MowerInstructionRequestToMowerInstructionConverterTest {

    @InjectMocks
    private MowerInstructionRequestToMowerInstructionConverter underTest;

    @ParameterizedTest
    @MethodSource("getMowerInstructionsForMappingTest")
    void convert_shouldMapMowerInstructionRequestIntoMowerInstruction(final MowerInstruction sourceInstruction,
                                                                      final com.seat.code.service.mower.model.MowerInstruction targetInstruction) {
        assertThat(underTest.convert(sourceInstruction)).isEqualTo(targetInstruction);
    }

    private static Stream<Arguments> getMowerInstructionsForMappingTest() {
        return Stream.of(buildMowerInstructionForMappingTest(SPIN_LEFT, com.seat.code.service.mower.model.MowerInstruction.SPIN_LEFT),
            buildMowerInstructionForMappingTest(SPIN_RIGHT, com.seat.code.service.mower.model.MowerInstruction.SPIN_RIGHT),
            buildMowerInstructionForMappingTest(MOVE_FORWARD, com.seat.code.service.mower.model.MowerInstruction.MOVE_FORWARD));
    }
    
    private static Arguments buildMowerInstructionForMappingTest(final MowerInstruction sourceInstruction,
                                                                 final com.seat.code.service.mower.model.MowerInstruction targetInstruction) {
        return Arguments.of(sourceInstruction, targetInstruction);
    }
}
