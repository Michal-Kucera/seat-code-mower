package com.seat.code.service;

import static com.seat.code.util.TestDomainLayerObjectFactory.buildPlateauEntity;
import static com.seat.code.util.TestServiceLayerObjectFactory.buildPlateau;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.domain.repository.PlateauRepository;
import com.seat.code.service.exception.PlateauNotFoundException;
import com.seat.code.service.mapper.ServiceLayerMapper;
import com.seat.code.service.model.Plateau;

@ExtendWith(MockitoExtension.class)
class PlateauServiceImplTest {

    @Mock
    private ServiceLayerMapper serviceLayerMapper;

    @Mock
    private PlateauRepository plateauRepository;

    @InjectMocks
    private PlateauServiceImpl underTest;

    @Test
    void createPlateau_shouldCreatePlateau() {
        final Plateau plateau = buildPlateau();
        final PlateauEntity plateauToStore = buildPlateauEntity();
        final PlateauEntity storedPlateau = buildPlateauEntity();

        when(serviceLayerMapper.mapToPlateauEntity(plateau)).thenReturn(plateauToStore);
        when(plateauRepository.save(plateauToStore)).thenReturn(storedPlateau);

        underTest.createPlateau(plateau);

        verify(serviceLayerMapper).mapToPlateauEntity(plateau);
        verify(plateauRepository).save(plateauToStore);
        verifyNoMoreInteractions(serviceLayerMapper, plateauRepository);
    }

    @Test
    void createPlateau_shouldReturnNewPlateauId_whenPlateauWasCreated() {
        final Plateau plateau = buildPlateau();
        final PlateauEntity plateauToStore = buildPlateauEntity();
        final PlateauEntity storedPlateau = buildPlateauEntity();

        when(serviceLayerMapper.mapToPlateauEntity(plateau)).thenReturn(plateauToStore);
        when(plateauRepository.save(plateauToStore)).thenReturn(storedPlateau);

        final UUID newPlateauId = underTest.createPlateau(plateau);

        assertThat(newPlateauId).isEqualTo(storedPlateau.getId());
        verify(serviceLayerMapper).mapToPlateauEntity(plateau);
        verify(plateauRepository).save(plateauToStore);
        verifyNoMoreInteractions(serviceLayerMapper, plateauRepository);
    }

    @Test
    void getPlateau_shouldReturnPlateau_whenPlateauFoundInDatabase() {
        final UUID plateauId = UUID.randomUUID();
        final PlateauEntity storedPlateau = buildPlateauEntity();
        final Plateau plateau = buildPlateau();

        when(plateauRepository.findById(plateauId)).thenReturn(Optional.of(storedPlateau));
        when(serviceLayerMapper.mapToPlateau(storedPlateau)).thenReturn(plateau);

        assertThat(underTest.getPlateau(plateauId)).isEqualTo(plateau);
        verify(plateauRepository).findById(plateauId);
        verify(serviceLayerMapper).mapToPlateau(storedPlateau);
        verifyNoMoreInteractions(plateauRepository, serviceLayerMapper);
    }

    @Test
    void getPlateau_shouldThrowPlateauNotFoundException_whenPlateauNotFoundInDatabase() {
        final UUID plateauId = UUID.randomUUID();

        when(plateauRepository.findById(plateauId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getPlateau(plateauId))
            .isInstanceOf(PlateauNotFoundException.class)
            .hasMessage("Plateau with ID: [%s] not found", plateauId);

        verify(plateauRepository).findById(plateauId);
        verifyNoMoreInteractions(plateauRepository);
    }
}
