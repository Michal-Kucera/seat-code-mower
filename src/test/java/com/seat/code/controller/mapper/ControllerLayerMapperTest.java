package com.seat.code.controller.mapper;

import static com.seat.code.util.TestControllerLayerObjectFactory.buildRectangularPlateau;
import static com.seat.code.util.TestServiceLayerObjectFactory.buildPlateau;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.asserts.AssertPlateau;
import com.seat.code.asserts.AssertRectangularPlateauDetail;
import com.seat.code.controller.model.RectangularPlateau;
import com.seat.code.controller.model.RectangularPlateauDetail;
import com.seat.code.service.model.Plateau;

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
            .hasNullCreatedDateTime()
            .hasName(rectangularPlateau.getName())
            .hasLength(rectangularPlateau.getSize().getLength())
            .hasWidth(rectangularPlateau.getSize().getWidth())
            .hasNoMowers();
    }

    @Test
    void mapToPlateauDetail_shouldMapPlateauIntoRectangularPlateauDetail() {
        final Plateau plateau = buildPlateau();
        final ArrayList<UUID> mowers = new ArrayList<>(plateau.getMowerIds());

        final RectangularPlateauDetail rectangularPlateauDetail = underTest.mapToPlateauDetail(plateau);

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
}
