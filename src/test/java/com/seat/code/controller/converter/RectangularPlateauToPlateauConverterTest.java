package com.seat.code.controller.converter;

import static com.seat.code.util.TestControllerLayerObjectFactory.buildRectangularPlateau;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seat.code.asserts.AssertPlateau;
import com.seat.code.controller.model.RectangularPlateau;
import com.seat.code.service.plateau.model.Plateau;

@ExtendWith(MockitoExtension.class)
class RectangularPlateauToPlateauConverterTest {

    @InjectMocks
    private RectangularPlateauToPlateauConverter underTest;

    @Test
    void convert_shouldMapRectangularPlateauIntoPlateau() {
        final RectangularPlateau rectangularPlateau = buildRectangularPlateau();

        final Plateau plateau = underTest.convert(rectangularPlateau);

        AssertPlateau.assertThat(plateau)
            .isNotNull()
            .hasNullId()
            .hasName(rectangularPlateau.getName())
            .hasLength(rectangularPlateau.getSize().getLength())
            .hasWidth(rectangularPlateau.getSize().getWidth())
            .hasNoMowers();
    }
}
