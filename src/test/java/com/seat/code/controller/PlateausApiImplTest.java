package com.seat.code.controller;

import static com.seat.code.util.TestControllerLayerObjectFactory.buildRectangularPlateau;
import static com.seat.code.util.TestJsonUtils.asJsonString;
import static com.seat.code.util.TestServiceLayerObjectFactory.buildPlateau;
import static io.restassured.RestAssured.baseURI;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import com.seat.code.controller.mapper.RectangularPlateauMapper;
import com.seat.code.controller.model.RectangularPlateau;
import com.seat.code.service.PlateauService;
import com.seat.code.service.model.Plateau;

@SpringBootTest
@AutoConfigureMockMvc
class PlateausApiImplTest {

    private static final String CREATE_PLATEAU_RESOURCE_PATH = "/v1/plateaus";
    private static final String GET_PLATEAU_RESOURCE_PATH = "/v1/plateaus/{plateauId}";

    @MockBean
    private RectangularPlateauMapper rectangularPlateauMapper;

    @MockBean
    private PlateauService plateauService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createPlateau_shouldCreatePlateau() throws Exception {
        final RectangularPlateau rectangularPlateau = buildRectangularPlateau();
        final Plateau plateau = buildPlateau();
        final UUID newPlateauId = UUID.randomUUID();

        when(rectangularPlateauMapper.mapToPlateau(rectangularPlateau)).thenReturn(plateau);
        when(plateauService.createPlateau(plateau)).thenReturn(newPlateauId);

        mockMvc.perform(post(CREATE_PLATEAU_RESOURCE_PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(asJsonString(rectangularPlateau)))
            .andDo(print());

        verify(rectangularPlateauMapper).mapToPlateau(rectangularPlateau);
        verify(plateauService).createPlateau(plateau);
        verifyNoMoreInteractions(rectangularPlateauMapper, plateauService);
    }

    @Test
    void createPlateau_shouldReturnCreatedHttpStatus_whenPlateauIsCreated() throws Exception {
        final RectangularPlateau rectangularPlateau = buildRectangularPlateau();
        final Plateau plateau = buildPlateau();
        final UUID newPlateauId = UUID.randomUUID();

        when(rectangularPlateauMapper.mapToPlateau(rectangularPlateau)).thenReturn(plateau);
        when(plateauService.createPlateau(plateau)).thenReturn(newPlateauId);

        mockMvc.perform(post(CREATE_PLATEAU_RESOURCE_PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(asJsonString(rectangularPlateau)))
            .andDo(print())
            .andExpect(status().isCreated());

        verify(rectangularPlateauMapper).mapToPlateau(rectangularPlateau);
        verify(plateauService).createPlateau(plateau);
        verifyNoMoreInteractions(rectangularPlateauMapper, plateauService);
    }

    @Test
    void createPlateau_shouldReturnLocationHeader_whenPlateauIsCreated() throws Exception {
        final RectangularPlateau rectangularPlateau = buildRectangularPlateau();
        final Plateau plateau = buildPlateau();
        final UUID newPlateauId = UUID.randomUUID();

        when(rectangularPlateauMapper.mapToPlateau(rectangularPlateau)).thenReturn(plateau);
        when(plateauService.createPlateau(plateau)).thenReturn(newPlateauId);

        mockMvc.perform(post(CREATE_PLATEAU_RESOURCE_PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(asJsonString(rectangularPlateau)))
            .andDo(print())
            .andExpect(header().string(LOCATION, buildPlateauLocationHeaderValue(newPlateauId)));

        verify(rectangularPlateauMapper).mapToPlateau(rectangularPlateau);
        verify(plateauService).createPlateau(plateau);
        verifyNoMoreInteractions(rectangularPlateauMapper, plateauService);
    }

    private String buildPlateauLocationHeaderValue(final UUID newPlateauId) {
        return UriComponentsBuilder.fromUriString(baseURI + GET_PLATEAU_RESOURCE_PATH)
            .build(newPlateauId)
            .toString();
    }
}
