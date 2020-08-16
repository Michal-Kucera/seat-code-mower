package com.seat.code.service.mapper;

import static com.seat.code.util.TestDomainLayerObjectFactory.buildMowerEntity;
import static com.seat.code.util.TestDomainLayerObjectFactory.buildPlateauEntity;
import static com.seat.code.util.TestServiceLayerObjectFactory.buildMower;
import static com.seat.code.util.TestServiceLayerObjectFactory.buildPlateau;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.asserts.AssertMower;
import com.seat.code.asserts.AssertMowerEntity;
import com.seat.code.asserts.AssertPlateau;
import com.seat.code.asserts.AssertPlateauEntity;
import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.MowerEntityOrientation;
import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.service.model.Mower;
import com.seat.code.service.model.MowerOrientation;
import com.seat.code.service.model.Plateau;

@ExtendWith(MockitoExtension.class)
class ServiceLayerMapperTest {

    @InjectMocks
    private ServiceLayerMapper underTest;

    @Test
    void mapToPlateau_shouldMapPlateauEntityIntoPlateau() {
        final PlateauEntity plateauEntity = buildPlateauEntity();

        final ArrayList<MowerEntity> mowerEntities = new ArrayList<>(plateauEntity.getMowers());

        final Plateau plateau = underTest.mapToPlateau(plateauEntity);

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

    @Test
    void mapToPlateauEntity_shouldMapPlateauIntoPlateauEntity() {
        final Plateau plateau = buildPlateau();

        final PlateauEntity plateauEntity = underTest.mapToPlateauEntity(plateau);

        AssertPlateauEntity.assertThat(plateauEntity)
            .isNotNull()
            .hasId(plateau.getId())
            .hasName(plateau.getName())
            .hasLength(plateau.getLength())
            .hasWidth(plateau.getWidth())
            .hasNoMowers();
    }

    @Test
    void mapToMowerEntity_shouldMapMowerAndPlateauEntityIntoMowerEntity() {
        final Mower mower = buildMower();
        final PlateauEntity plateauEntity = buildPlateauEntity();

        final MowerEntity mowerEntity = underTest.mapToMowerEntity(mower, plateauEntity);

        AssertMowerEntity.assertThat(mowerEntity)
            .isNotNull()
            .hasNullId()
            .hasNullVersion()
            .hasName(mower.getName())
            .hasPlateau(plateauEntity)
            .hasLatitude(mower.getLatitude())
            .hasLongitude(mower.getLongitude())
            .hasOrientation(MowerEntityOrientation.valueOf(mower.getOrientation().name()));
    }

    @Test
    void mapToMower_shouldMapMowerEntityIntoMower() {
        final MowerEntity mowerEntity = buildMowerEntity();

        final Mower mower = underTest.mapToMower(mowerEntity);

        AssertMower.assertThat(mower)
            .isNotNull()
            .hasId(mowerEntity.getId())
            .hasName(mowerEntity.getName())
            .hasPlateauId(mowerEntity.getPlateau().getId())
            .hasLatitude(mowerEntity.getLatitude())
            .hasLongitude(mowerEntity.getLongitude())
            .hasOrientation(MowerOrientation.valueOf(mowerEntity.getOrientation().name()));
    }
}
