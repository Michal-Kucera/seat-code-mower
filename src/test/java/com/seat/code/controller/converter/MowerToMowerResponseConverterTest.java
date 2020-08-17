package com.seat.code.controller.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.asserts.AssertMowerResponse;
import com.seat.code.controller.model.Mower;
import com.seat.code.util.TestServiceLayerObjectFactory;

@ExtendWith(MockitoExtension.class)
class MowerToMowerResponseConverterTest {

    @InjectMocks
    private MowerToMowerResponseConverter underTest;

    @Test
    void convert_shouldMapMowerIntoMowerResponse() {
        final com.seat.code.service.mower.model.Mower mower = TestServiceLayerObjectFactory.buildMower();

        final Mower mowerResponse = underTest.convert(mower);

        AssertMowerResponse.assertThat(mowerResponse)
            .isNotNull()
            .hasName(mower.getName())
            .hasLatitude(mower.getLatitude())
            .hasLongitude(mower.getLongitude())
            .hasOrientation(com.seat.code.controller.model.MowerOrientation.valueOf(mower.getOrientation().name()));
    }
}
