package com.seat.code.service.mapper;

import static com.seat.code.util.TestServiceLayerObjectFactory.buildPlateau;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.asserts.AssertPlateauEntity;
import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.service.model.Plateau;

@ExtendWith(MockitoExtension.class)
class PlateauMapperTest {

    @InjectMocks
    private PlateauMapper underTest;

    @Test
    void mapToPlateauEntity_shouldMapPlateauIntoPlateauEntity() {
        final Plateau plateau = buildPlateau();

        final PlateauEntity plateauEntity = underTest.mapToPlateauEntity(plateau);

        AssertPlateauEntity.assertThat(plateauEntity)
            .isNotNull()
            .hasId(plateau.getId())
            .hasCreatedDateTime(plateau.getCreatedDateTime())
            .hasName(plateau.getName())
            .hasLength(plateau.getLength())
            .hasWidth(plateau.getWidth())
            .hasNoMowers();
    }
}
