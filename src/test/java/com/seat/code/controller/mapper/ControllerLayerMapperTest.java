package com.seat.code.controller.mapper;

import static com.seat.code.util.TestControllerLayerObjectFactory.buildRectangularPlateau;
import static com.seat.code.util.TestServiceLayerObjectFactory.buildPlateau;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.asserts.AssertMower;
import com.seat.code.asserts.AssertMowerResponse;
import com.seat.code.asserts.AssertPlateau;
import com.seat.code.asserts.AssertRectangularPlateauDetail;
import com.seat.code.controller.model.Mower;
import com.seat.code.controller.model.RectangularPlateau;
import com.seat.code.controller.model.RectangularPlateauDetail;
import com.seat.code.service.model.MowerOrientation;
import com.seat.code.service.model.Plateau;
import com.seat.code.util.TestControllerLayerObjectFactory;
import com.seat.code.util.TestServiceLayerObjectFactory;

@ExtendWith(MockitoExtension.class)
class ControllerLayerMapperTest {

    @InjectMocks
    private ControllerLayerMapper underTest;

    @Test
    void mapToPlateau_shouldMapRectangularPlateauIntoPlateau() {
        final RectangularPlateau rectangularPlateau = buildRectangularPlateau();

        final Plateau plateau = underTest.mapToPlateau(rectangularPlateau);

        AssertPlateau.assertThat(plateau)
            .isNotNull()
            .hasNullId()
            .hasName(rectangularPlateau.getName())
            .hasLength(rectangularPlateau.getSize().getLength())
            .hasWidth(rectangularPlateau.getSize().getWidth())
            .hasNoMowers();
    }

    @Test
    void mapToPlateauDetailResponse_shouldMapPlateauIntoRectangularPlateauDetail() {
        final Plateau plateau = buildPlateau();
        final ArrayList<UUID> mowers = new ArrayList<>(plateau.getMowerIds());

        final RectangularPlateauDetail rectangularPlateauDetail = underTest.mapToPlateauDetailResponse(plateau);

        AssertRectangularPlateauDetail.assertThat(rectangularPlateauDetail)
            .isNotNull()
            .hasName(plateau.getName())
            .hasLength(plateau.getLength())
            .hasWidth(plateau.getWidth())
            .hasMowersSize(plateau.getMowerIds().size())
            .hasMowers(mowers.get(0))
            .hasMowers(mowers.get(1))
            .hasMowers(mowers.get(2))
            .hasMowers(mowers.get(3));
    }

    @Test
    void mapToMower_shouldMapPlateauIdAndMowerRequestIntoMower() {
        final UUID plateauId = UUID.randomUUID();
        final Mower mowerRequest = TestControllerLayerObjectFactory.buildMower();

        final com.seat.code.service.model.Mower mower = underTest.mapToMower(plateauId, mowerRequest);

        AssertMower.assertThat(mower)
            .isNotNull()
            .hasNullId()
            .hasName(mowerRequest.getName())
            .hasPlateauId(plateauId)
            .hasLatitude(mowerRequest.getPosition().getLatitude())
            .hasLongitude(mowerRequest.getPosition().getLongitude())
            .hasOrientation(MowerOrientation.valueOf(mowerRequest.getPosition().getOrientation().name()));
    }

    @Test
    void mapToMowerResponse_shouldMapMowerIntoMowerResponse() {
        final com.seat.code.service.model.Mower mower = TestServiceLayerObjectFactory.buildMower();

        final Mower mowerResponse = underTest.mapToMowerResponse(mower);

        AssertMowerResponse.assertThat(mowerResponse)
            .isNotNull()
            .hasName(mower.getName())
            .hasLatitude(mower.getLatitude())
            .hasLongitude(mower.getLongitude())
            .hasOrientation(com.seat.code.controller.model.MowerOrientation.valueOf(mower.getOrientation().name()));
    }
}
