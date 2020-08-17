package com.seat.code.service.mower.converter;

import static com.seat.code.util.TestDomainLayerObjectFactory.buildMowerEntity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.asserts.AssertMower;
import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.service.mower.model.Mower;
import com.seat.code.service.mower.model.MowerOrientation;

@ExtendWith(MockitoExtension.class)
class MowerEntityToMowerConverterTest {

    @InjectMocks
    private MowerEntityToMowerConverter underTest;

    @Test
    void convert_shouldMapMowerEntityIntoMower() {
        final MowerEntity mowerEntity = buildMowerEntity();

        final Mower mower = underTest.convert(mowerEntity);

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
