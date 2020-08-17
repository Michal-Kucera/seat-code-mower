package com.seat.code.service.mower.converter;

import static com.seat.code.util.TestServiceLayerObjectFactory.buildMower;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.asserts.AssertMowerEntity;
import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.MowerEntityOrientation;
import com.seat.code.service.mower.model.Mower;

@ExtendWith(MockitoExtension.class)
class MowerToMowerEntityConverterTest {

    @InjectMocks
    private MowerToMowerEntityConverter underTest;

    @Test
    void convert_shouldMapMowerIntoMowerEntity() {
        final Mower mower = buildMower();

        final MowerEntity mowerEntity = underTest.convert(mower);

        AssertMowerEntity.assertThat(mowerEntity)
            .isNotNull()
            .hasNullId()
            .hasNullVersion()
            .hasName(mower.getName())
            .hasNullPlateau()
            .hasLatitude(mower.getLatitude())
            .hasLongitude(mower.getLongitude())
            .hasOrientation(MowerEntityOrientation.valueOf(mower.getOrientation().name()));
    }
}
