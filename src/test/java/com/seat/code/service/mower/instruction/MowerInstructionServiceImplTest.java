package com.seat.code.service.mower.instruction;

import static com.seat.code.service.mower.model.MowerInstruction.MOVE_FORWARD;
import static com.seat.code.service.mower.model.MowerInstruction.SPIN_LEFT;
import static com.seat.code.service.mower.model.MowerInstruction.SPIN_RIGHT;
import static com.seat.code.util.TestDomainLayerObjectFactory.buildMowerEntity;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.asserts.AssertMowerEntity;
import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.repository.MowerRepository;
import com.seat.code.service.mower.exception.MowerNotFoundException;
import com.seat.code.service.mower.instruction.processor.MowerInstructionProcessor;
import com.seat.code.service.mower.model.MowerInstruction;

@ExtendWith(MockitoExtension.class)
class MowerInstructionServiceImplTest {

    @Mock
    private MowerRepository mowerRepository;

    @Mock
    private MowerInstructionProcessor mowerSpinLeftInstructionProcessor;

    @Mock
    private MowerInstructionProcessor mowerSpinRightInstructionProcessor;

    @Mock
    private MowerInstructionProcessor mowerMoveForwardInstructionProcessor;

    private MowerInstructionServiceImpl underTest;

    @BeforeEach
    void setUp() {
        final List<MowerInstructionProcessor> mowerInstructionProcessors = asList(mowerSpinLeftInstructionProcessor,
            mowerSpinRightInstructionProcessor,
            mowerMoveForwardInstructionProcessor);
        underTest = new MowerInstructionServiceImpl(mowerRepository, mowerInstructionProcessors);
    }

    @Captor
    private ArgumentCaptor<MowerEntity> mowerEntityArgumentCaptor;

    @Test
    void applyInstructionsToMower_shouldApplySpinLeftInstructionToMower_whenMowerExistsAndIsAssociatedToTargetingPlateauAndInstructionIsSpinLeft() {
        final UUID plateauId = UUID.randomUUID();
        final UUID mowerId = UUID.randomUUID();
        final MowerInstruction spinLeftInstruction = SPIN_LEFT;
        final List<MowerInstruction> mowerInstructions = singletonList(spinLeftInstruction);
        final MowerEntity storedMowerEntity = buildMowerEntity();

        when(mowerRepository.findOneByIdAndPlateauId(mowerId, plateauId)).thenReturn(Optional.of(storedMowerEntity));
        when(mowerSpinLeftInstructionProcessor.isInstructionSupported(spinLeftInstruction)).thenReturn(true);
        when(mowerSpinRightInstructionProcessor.isInstructionSupported(spinLeftInstruction)).thenReturn(false);
        when(mowerMoveForwardInstructionProcessor.isInstructionSupported(spinLeftInstruction)).thenReturn(false);

        underTest.applyInstructionsToMower(mowerInstructions, plateauId, mowerId);

        verify(mowerRepository).findOneByIdAndPlateauId(mowerId, plateauId);
        verify(mowerSpinLeftInstructionProcessor).isInstructionSupported(spinLeftInstruction);
        verify(mowerSpinRightInstructionProcessor).isInstructionSupported(spinLeftInstruction);
        verify(mowerMoveForwardInstructionProcessor).isInstructionSupported(spinLeftInstruction);
        verify(mowerSpinLeftInstructionProcessor).applyInstruction(storedMowerEntity);
        verify(mowerRepository).save(mowerEntityArgumentCaptor.capture());
        AssertMowerEntity.assertThat(mowerEntityArgumentCaptor)
            .isNotNull()
            .hasId(storedMowerEntity.getId())
            .hasVersion(storedMowerEntity.getVersion())
            .hasName(storedMowerEntity.getName())
            .hasPlateau(storedMowerEntity.getPlateau())
            .hasLatitude(storedMowerEntity.getLatitude())
            .hasLongitude(storedMowerEntity.getLongitude())
            .hasOrientation(storedMowerEntity.getOrientation());
        verifyNoMoreInteractions(mowerRepository, mowerSpinLeftInstructionProcessor, mowerSpinRightInstructionProcessor, mowerMoveForwardInstructionProcessor);
    }

    @Test
    void applyInstructionsToMower_shouldApplySpinRightInstructionToMower_whenMowerExistsAndIsAssociatedToTargetingPlateauAndInstructionIsSpinRight() {
        final UUID plateauId = UUID.randomUUID();
        final UUID mowerId = UUID.randomUUID();
        final MowerInstruction spinRightInstruction = SPIN_RIGHT;
        final List<MowerInstruction> mowerInstructions = singletonList(spinRightInstruction);
        final MowerEntity storedMowerEntity = buildMowerEntity();

        when(mowerRepository.findOneByIdAndPlateauId(mowerId, plateauId)).thenReturn(Optional.of(storedMowerEntity));
        when(mowerSpinLeftInstructionProcessor.isInstructionSupported(spinRightInstruction)).thenReturn(false);
        when(mowerSpinRightInstructionProcessor.isInstructionSupported(spinRightInstruction)).thenReturn(true);
        when(mowerMoveForwardInstructionProcessor.isInstructionSupported(spinRightInstruction)).thenReturn(false);

        underTest.applyInstructionsToMower(mowerInstructions, plateauId, mowerId);

        verify(mowerRepository).findOneByIdAndPlateauId(mowerId, plateauId);
        verify(mowerSpinLeftInstructionProcessor).isInstructionSupported(spinRightInstruction);
        verify(mowerSpinRightInstructionProcessor).isInstructionSupported(spinRightInstruction);
        verify(mowerMoveForwardInstructionProcessor).isInstructionSupported(spinRightInstruction);
        verify(mowerSpinRightInstructionProcessor).applyInstruction(storedMowerEntity);
        verify(mowerRepository).save(mowerEntityArgumentCaptor.capture());
        AssertMowerEntity.assertThat(mowerEntityArgumentCaptor)
            .isNotNull()
            .hasId(storedMowerEntity.getId())
            .hasVersion(storedMowerEntity.getVersion())
            .hasName(storedMowerEntity.getName())
            .hasPlateau(storedMowerEntity.getPlateau())
            .hasLatitude(storedMowerEntity.getLatitude())
            .hasLongitude(storedMowerEntity.getLongitude())
            .hasOrientation(storedMowerEntity.getOrientation());
        verifyNoMoreInteractions(mowerRepository, mowerSpinLeftInstructionProcessor, mowerSpinRightInstructionProcessor, mowerMoveForwardInstructionProcessor);
    }

    @Test
    void applyInstructionsToMower_shouldApplyMoveForwardInstructionToMower_whenMowerExistsAndIsAssociatedToTargetingPlateauAndInstructionIsMoveForward() {
        final UUID plateauId = UUID.randomUUID();
        final UUID mowerId = UUID.randomUUID();
        final MowerInstruction moveForwardInstruction = MOVE_FORWARD;
        final List<MowerInstruction> mowerInstructions = singletonList(moveForwardInstruction);
        final MowerEntity storedMowerEntity = buildMowerEntity();

        when(mowerRepository.findOneByIdAndPlateauId(mowerId, plateauId)).thenReturn(Optional.of(storedMowerEntity));
        when(mowerSpinLeftInstructionProcessor.isInstructionSupported(moveForwardInstruction)).thenReturn(false);
        when(mowerSpinRightInstructionProcessor.isInstructionSupported(moveForwardInstruction)).thenReturn(false);
        when(mowerMoveForwardInstructionProcessor.isInstructionSupported(moveForwardInstruction)).thenReturn(true);

        underTest.applyInstructionsToMower(mowerInstructions, plateauId, mowerId);

        verify(mowerRepository).findOneByIdAndPlateauId(mowerId, plateauId);
        verify(mowerSpinLeftInstructionProcessor).isInstructionSupported(moveForwardInstruction);
        verify(mowerSpinRightInstructionProcessor).isInstructionSupported(moveForwardInstruction);
        verify(mowerMoveForwardInstructionProcessor).isInstructionSupported(moveForwardInstruction);
        verify(mowerMoveForwardInstructionProcessor).applyInstruction(storedMowerEntity);
        verify(mowerRepository).save(mowerEntityArgumentCaptor.capture());
        AssertMowerEntity.assertThat(mowerEntityArgumentCaptor)
            .isNotNull()
            .hasId(storedMowerEntity.getId())
            .hasVersion(storedMowerEntity.getVersion())
            .hasName(storedMowerEntity.getName())
            .hasPlateau(storedMowerEntity.getPlateau())
            .hasLatitude(storedMowerEntity.getLatitude())
            .hasLongitude(storedMowerEntity.getLongitude())
            .hasOrientation(storedMowerEntity.getOrientation());
        verifyNoMoreInteractions(mowerRepository, mowerSpinLeftInstructionProcessor, mowerSpinRightInstructionProcessor, mowerMoveForwardInstructionProcessor);
    }

    @Test
    void applyInstructionsToMower_shouldThrowMowerNotFoundException_whenMowerNotFoundInDatabase() {
        final UUID plateauId = UUID.randomUUID();
        final UUID mowerId = UUID.randomUUID();
        final List<MowerInstruction> mowerInstructions = singletonList(SPIN_LEFT);

        when(mowerRepository.findOneByIdAndPlateauId(mowerId, plateauId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.applyInstructionsToMower(mowerInstructions, plateauId, mowerId))
            .isInstanceOf(MowerNotFoundException.class)
            .hasMessage("Mower with ID: [%s], associated to plateau with ID: [%s] not found", mowerId, plateauId);

        verify(mowerRepository).findOneByIdAndPlateauId(mowerId, plateauId);
        verifyNoMoreInteractions(mowerRepository);
        verifyNoInteractions(mowerSpinLeftInstructionProcessor, mowerSpinRightInstructionProcessor, mowerMoveForwardInstructionProcessor);
    }
}
