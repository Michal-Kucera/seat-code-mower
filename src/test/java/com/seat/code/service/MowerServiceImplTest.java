package com.seat.code.service;

import static com.seat.code.util.TestDomainLayerObjectFactory.buildMowerEntity;
import static com.seat.code.util.TestDomainLayerObjectFactory.buildPlateauEntity;
import static com.seat.code.util.TestServiceLayerObjectFactory.buildMower;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.domain.repository.MowerRepository;
import com.seat.code.domain.repository.PlateauRepository;
import com.seat.code.service.exception.MowerPositionAlreadyTakenException;
import com.seat.code.service.exception.MowerPositionOutOfRangeException;
import com.seat.code.service.exception.PlateauNotFoundException;
import com.seat.code.service.mapper.ServiceLayerMapper;
import com.seat.code.service.model.Mower;

@ExtendWith(MockitoExtension.class)
class MowerServiceImplTest {

    @Mock
    private ServiceLayerMapper serviceLayerMapper;

    @Mock
    private PlateauRepository plateauRepository;

    @Mock
    private MowerRepository mowerRepository;

    @InjectMocks
    private MowerServiceImpl underTest;

    @ParameterizedTest
    @MethodSource("getPlateauAndMowerPositionsForMowerPositionNotOutOfRangeTest")
    void createMower_shouldStoreMowerIntoDatabase_whenPlateauFoundInDatabaseAndPositionIsNotOutOfRange(final Integer plateauLength,
                                                                                                       final Integer plateauWidth,
                                                                                                       final Integer mowerLatitude,
                                                                                                       final Integer mowerLongitude) {
        final Mower mower = buildMower(mowerLatitude, mowerLongitude);
        final PlateauEntity plateauEntity = buildPlateauEntity(plateauLength, plateauWidth);
        final MowerEntity mowerEntityToStore = buildMowerEntity(plateauEntity);
        final MowerEntity storedMowerEntity = buildMowerEntity(plateauEntity);

        when(plateauRepository.findById(mower.getPlateauId())).thenReturn(Optional.of(plateauEntity));
        when(serviceLayerMapper.mapToMowerEntity(mower, plateauEntity)).thenReturn(mowerEntityToStore);
        when(mowerRepository.save(mowerEntityToStore)).thenReturn(storedMowerEntity);

        underTest.createMower(mower);

        verify(plateauRepository).findById(mower.getPlateauId());
        verify(serviceLayerMapper).mapToMowerEntity(mower, plateauEntity);
        verify(mowerRepository).save(mowerEntityToStore);
        verifyNoMoreInteractions(plateauRepository, serviceLayerMapper, mowerRepository);
    }

    @Test
    void createMower_shouldReturnNewMowerId_whenMowerWasStoredIntoDatabase() {
        final Mower mower = buildMower();
        final PlateauEntity plateauEntity = buildPlateauEntity();
        final MowerEntity mowerEntityToStore = buildMowerEntity(plateauEntity);
        final MowerEntity storedMowerEntity = buildMowerEntity(plateauEntity);

        when(plateauRepository.findById(mower.getPlateauId())).thenReturn(Optional.of(plateauEntity));
        when(serviceLayerMapper.mapToMowerEntity(mower, plateauEntity)).thenReturn(mowerEntityToStore);
        when(mowerRepository.save(mowerEntityToStore)).thenReturn(storedMowerEntity);

        final UUID newMowerId = underTest.createMower(mower);

        assertThat(newMowerId).isEqualTo(storedMowerEntity.getId());
        verify(plateauRepository).findById(mower.getPlateauId());
        verify(serviceLayerMapper).mapToMowerEntity(mower, plateauEntity);
        verify(mowerRepository).save(mowerEntityToStore);
        verifyNoMoreInteractions(plateauRepository, serviceLayerMapper, mowerRepository);
    }

    @Test
    void createMower_shouldThrowPlateauNotFoundException_whenPlateauNotFoundInDatabase() {
        final Mower mower = buildMower();

        when(plateauRepository.findById(mower.getPlateauId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.createMower(mower))
            .isInstanceOf(PlateauNotFoundException.class)
            .hasMessage("Plateau with ID: [%s] not found", mower.getPlateauId());

        verify(plateauRepository).findById(mower.getPlateauId());
        verifyNoMoreInteractions(plateauRepository);
        verifyNoInteractions(serviceLayerMapper, mowerRepository);
    }

    @ParameterizedTest
    @MethodSource("getPlateauAndMowerPositionsForMowerPositionOutOfRangeExceptionTest")
    void createMower_shouldThrowMowerPositionOutOfRangeException_whenPlateauFoundInDatabaseButMowerIsOutOfRange(final Integer plateauLength,
                                                                                                                final Integer plateauWidth,
                                                                                                                final Integer mowerLatitude,
                                                                                                                final Integer mowerLongitude) {
        final Mower mower = buildMower(mowerLatitude, mowerLongitude);
        final PlateauEntity plateauEntity = buildPlateauEntity(plateauLength, plateauWidth);

        when(plateauRepository.findById(mower.getPlateauId())).thenReturn(Optional.of(plateauEntity));

        assertThatThrownBy(() -> underTest.createMower(mower))
            .isInstanceOf(MowerPositionOutOfRangeException.class)
            .hasMessage("Mower's position is out of range of plateau");

        verify(plateauRepository).findById(mower.getPlateauId());
        verifyNoMoreInteractions(plateauRepository);
        verifyNoInteractions(serviceLayerMapper, mowerRepository);
    }

    @Test
    void createMower_shouldThrowMowerPositionAlreadyTakenException_whenPlateauFoundInDatabaseButMowerIsTargetingTheSamePositionAsAlreadyExistingMowerInPlateau() {
        final PlateauEntity plateauEntity = buildPlateauEntity();
        plateauEntity.getMowers().clear();
        final Integer mowerLatitude = 3;
        final Integer mowerLongitude = 2;
        final MowerEntity alreadyExistingMowerWithSamePosition = buildMowerEntity(plateauEntity, mowerLatitude, mowerLongitude);
        plateauEntity.getMowers().add(alreadyExistingMowerWithSamePosition);
        final Mower mower = buildMower(mowerLatitude, mowerLongitude);

        when(plateauRepository.findById(mower.getPlateauId())).thenReturn(Optional.of(plateauEntity));

        assertThatThrownBy(() -> underTest.createMower(mower))
            .isInstanceOf(MowerPositionAlreadyTakenException.class)
            .hasMessage("Mower's targeting position is already taken by another mower in plateau");

        verify(plateauRepository).findById(mower.getPlateauId());
        verifyNoMoreInteractions(plateauRepository);
        verifyNoInteractions(serviceLayerMapper, mowerRepository);
    }

    private static Stream<Arguments> getPlateauAndMowerPositionsForMowerPositionNotOutOfRangeTest() {
        return Stream.of(buildPlateauAndMowerPositionArgument(5, 5, 5, 5),
            buildPlateauAndMowerPositionArgument(5, 5, 3, 3),
            buildPlateauAndMowerPositionArgument(3, 5, 3, 5),
            buildPlateauAndMowerPositionArgument(5, 3, 5, 3));
    }

    private static Stream<Arguments> getPlateauAndMowerPositionsForMowerPositionOutOfRangeExceptionTest() {
        return Stream.of(buildPlateauAndMowerPositionArgument(5, 3, 6, 3),
            buildPlateauAndMowerPositionArgument(5, 3, 5, 4),
            buildPlateauAndMowerPositionArgument(5, 3, 6, 4));
    }

    private static Arguments buildPlateauAndMowerPositionArgument(final int plateauLength,
                                                                  final int plateauWidth,
                                                                  final int mowerLatitude,
                                                                  final int mowerLongitude) {
        return Arguments.of(plateauLength, plateauWidth, mowerLatitude, mowerLongitude);
    }
}
