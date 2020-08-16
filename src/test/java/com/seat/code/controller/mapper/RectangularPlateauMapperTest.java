package com.seat.code.controller.mapper;

import static com.seat.code.util.TestControllerLayerObjectFactory.buildRectangularPlateau;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.asserts.AssertPlateau;
import com.seat.code.controller.model.RectangularPlateau;
import com.seat.code.service.model.Plateau;

@ExtendWith(MockitoExtension.class)
class RectangularPlateauMapperTest {

    @InjectMocks
    private RectangularPlateauMapper underTest;

    @Test
    void mapToPlateau_shouldMapRectangularPlateauIntoPlateau() {
        final RectangularPlateau rectangularPlateau = buildRectangularPlateau();

        final Plateau plateau = underTest.mapToPlateau(rectangularPlateau);

        AssertPlateau.assertThat(plateau)
            .isNotNull()
            .hasNullId()
            .hasNullCreationDateTime()
            .hasName(rectangularPlateau.getName())
            .hasLength(rectangularPlateau.getSize().getLength())
            .hasWidth(rectangularPlateau.getSize().getWidth())
            .hasNoMowers();
    }
}
