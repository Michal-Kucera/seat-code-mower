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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.domain.repository.MowerRepository;
import com.seat.code.domain.repository.PlateauRepository;
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

    @Test
    void createMower_shouldStoreMowerIntoDatabase_whenPlateauFoundInDatabase() {
        final Mower mower = buildMower();
        final PlateauEntity plateauEntity = buildPlateauEntity();
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
}
