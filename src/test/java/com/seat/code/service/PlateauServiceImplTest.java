package com.seat.code.service;

import static com.seat.code.util.TestDomainLayerObjectFactory.buildPlateauEntity;
import static com.seat.code.util.TestServiceLayerObjectFactory.buildPlateau;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.domain.repository.PlateauRepository;
import com.seat.code.service.mapper.PlateauMapper;
import com.seat.code.service.model.Plateau;

@ExtendWith(MockitoExtension.class)
class PlateauServiceImplTest {

    @Mock
    private PlateauMapper plateauMapper;

    @Mock
    private PlateauRepository plateauRepository;

    @InjectMocks
    private PlateauServiceImpl underTest;

    @Test
    void createPlateau_shouldCreatePlateau() {
        final Plateau plateau = buildPlateau();
        final PlateauEntity plateauToStore = buildPlateauEntity();
        final PlateauEntity storedPlateau = buildPlateauEntity();

        when(plateauMapper.mapToPlateauEntity(plateau)).thenReturn(plateauToStore);
        when(plateauRepository.save(plateauToStore)).thenReturn(storedPlateau);

        underTest.createPlateau(plateau);

        verify(plateauMapper).mapToPlateauEntity(plateau);
        verify(plateauRepository).save(plateauToStore);
        verifyNoMoreInteractions(plateauMapper, plateauRepository);
    }

    @Test
    void createPlateau_shouldReturnNewPlateauId_whenPlateauWasCreated() {
        final Plateau plateau = buildPlateau();
        final PlateauEntity plateauToStore = buildPlateauEntity();
        final PlateauEntity storedPlateau = buildPlateauEntity();

        when(plateauMapper.mapToPlateauEntity(plateau)).thenReturn(plateauToStore);
        when(plateauRepository.save(plateauToStore)).thenReturn(storedPlateau);

        final UUID newPlateauId = underTest.createPlateau(plateau);

        assertThat(newPlateauId).isEqualTo(storedPlateau.getId());
        verify(plateauMapper).mapToPlateauEntity(plateau);
        verify(plateauRepository).save(plateauToStore);
        verifyNoMoreInteractions(plateauMapper, plateauRepository);
    }
}
