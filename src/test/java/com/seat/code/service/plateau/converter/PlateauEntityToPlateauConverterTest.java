package com.seat.code.service.plateau.converter;

import static com.seat.code.util.TestDomainLayerObjectFactory.buildPlateauEntity;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.asserts.AssertPlateau;
import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.service.plateau.model.Plateau;

@ExtendWith(MockitoExtension.class)
class PlateauEntityToPlateauConverterTest {

    @InjectMocks
    private PlateauEntityToPlateauConverter underTest;

    @Test
    void convert_shouldMapPlateauEntityIntoPlateau() {
        final PlateauEntity plateauEntity = buildPlateauEntity();

        final ArrayList<MowerEntity> mowerEntities = new ArrayList<>(plateauEntity.getMowers());

        final Plateau plateau = underTest.convert(plateauEntity);

        AssertPlateau.assertThat(plateau)
            .isNotNull()
            .hasId(plateauEntity.getId())
            .hasName(plateauEntity.getName())
            .hasLength(plateauEntity.getLength())
            .hasWidth(plateauEntity.getWidth())
            .hasMowerIdsSize(4)
            .hasMowerId(mowerEntities.get(0).getId())
            .hasMowerId(mowerEntities.get(1).getId())
            .hasMowerId(mowerEntities.get(2).getId())
            .hasMowerId(mowerEntities.get(3).getId());
    }
}
