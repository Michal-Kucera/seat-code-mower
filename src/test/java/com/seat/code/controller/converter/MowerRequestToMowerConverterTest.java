package com.seat.code.controller.converter;

import static com.seat.code.util.TestControllerLayerObjectFactory.buildMower;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.asserts.AssertMower;
import com.seat.code.controller.model.Mower;
import com.seat.code.service.mower.model.MowerOrientation;

@ExtendWith(MockitoExtension.class)
class MowerRequestToMowerConverterTest {

    @InjectMocks
    private MowerRequestToMowerConverter underTest;

    @Test
    void convert_shouldMapMowerRequestIntoMower() {
        final Mower mowerRequest = buildMower();

        final com.seat.code.service.mower.model.Mower mower = underTest.convert(mowerRequest);

        AssertMower.assertThat(mower)
            .isNotNull()
            .hasNullId()
            .hasName(mowerRequest.getName())
            .hasNullPlateauId()
            .hasLatitude(mowerRequest.getPosition().getLatitude())
            .hasLongitude(mowerRequest.getPosition().getLongitude())
            .hasOrientation(MowerOrientation.valueOf(mowerRequest.getPosition().getOrientation().name()));
    }
}
