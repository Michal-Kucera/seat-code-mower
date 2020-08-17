package com.seat.code.service.plateau;

import static com.seat.code.util.TestDomainLayerObjectFactory.buildPlateauEntity;
import static com.seat.code.util.TestServiceLayerObjectFactory.buildPlateau;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
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
import org.springframework.core.convert.ConversionService;

import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.domain.repository.PlateauRepository;
import com.seat.code.service.plateau.exception.PlateauNotFoundException;
import com.seat.code.service.plateau.model.Plateau;

@ExtendWith(MockitoExtension.class)
class PlateauServiceImplTest {

    @Mock
    private ConversionService conversionService;

    @Mock
    private PlateauRepository plateauRepository;

    @InjectMocks
    private PlateauServiceImpl underTest;

    @Test
    void createPlateau_shouldCreatePlateau() {
        final Plateau plateau = buildPlateau();
        final PlateauEntity plateauToStore = buildPlateauEntity();
        final PlateauEntity storedPlateau = buildPlateauEntity();

        doReturn(plateauToStore).when(conversionService).convert(plateau, PlateauEntity.class);
        when(plateauRepository.save(plateauToStore)).thenReturn(storedPlateau);

        underTest.createPlateau(plateau);

        verify(conversionService).convert(plateau, PlateauEntity.class);
        verify(plateauRepository).save(plateauToStore);
        verifyNoMoreInteractions(plateauRepository);
    }

    @Test
    void createPlateau_shouldReturnNewPlateauId_whenPlateauWasCreated() {
        final Plateau plateau = buildPlateau();
        final PlateauEntity plateauToStore = buildPlateauEntity();
        final PlateauEntity storedPlateau = buildPlateauEntity();

        doReturn(plateauToStore).when(conversionService).convert(plateau, PlateauEntity.class);
        when(plateauRepository.save(plateauToStore)).thenReturn(storedPlateau);

        final UUID newPlateauId = underTest.createPlateau(plateau);

        assertThat(newPlateauId).isEqualTo(storedPlateau.getId());
        verify(conversionService).convert(plateau, PlateauEntity.class);
        verify(plateauRepository).save(plateauToStore);
        verifyNoMoreInteractions(plateauRepository);
    }

    @Test
    void getPlateau_shouldReturnPlateau_whenPlateauFoundInDatabase() {
        final UUID plateauId = UUID.randomUUID();
        final PlateauEntity storedPlateau = buildPlateauEntity();
        final Plateau plateau = buildPlateau();

        when(plateauRepository.findById(plateauId)).thenReturn(Optional.of(storedPlateau));
        doReturn(plateau).when(conversionService).convert(storedPlateau, Plateau.class);

        assertThat(underTest.getPlateau(plateauId)).isEqualTo(plateau);
        verify(plateauRepository).findById(plateauId);
        verify(conversionService).convert(storedPlateau, Plateau.class);
        verifyNoMoreInteractions(plateauRepository);
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
