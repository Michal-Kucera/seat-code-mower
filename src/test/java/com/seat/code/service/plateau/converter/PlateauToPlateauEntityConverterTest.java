package com.seat.code.service.plateau.converter;

import static com.seat.code.util.TestServiceLayerObjectFactory.buildPlateau;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.asserts.AssertPlateauEntity;
import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.service.plateau.model.Plateau;

@ExtendWith(MockitoExtension.class)
class PlateauToPlateauEntityConverterTest {

    @InjectMocks
    private PlateauToPlateauEntityConverter underTest;

    @Test
    void convert_shouldMapPlateauIntoPlateauEntity() {
        final Plateau plateau = buildPlateau();

        final PlateauEntity plateauEntity = underTest.convert(plateau);

        AssertPlateauEntity.assertThat(plateauEntity)
            .isNotNull()
            .hasId(plateau.getId())
            .hasName(plateau.getName())
            .hasLength(plateau.getLength())
            .hasWidth(plateau.getWidth())
            .hasNoMowers();
    }
}
