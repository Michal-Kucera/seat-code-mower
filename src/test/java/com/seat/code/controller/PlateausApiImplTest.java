package com.seat.code.controller;

import static com.seat.code.util.TestControllerLayerObjectFactory.buildRectangularPlateau;
import static com.seat.code.util.TestControllerLayerObjectFactory.buildRectangularPlateauDetail;
import static com.seat.code.util.TestJsonUtils.asJsonString;
import static com.seat.code.util.TestServiceLayerObjectFactory.buildPlateau;
import static io.restassured.RestAssured.baseURI;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import com.seat.code.controller.mapper.ControllerLayerMapper;
import com.seat.code.controller.model.RectangularPlateau;
import com.seat.code.controller.model.RectangularPlateauDetail;
import com.seat.code.service.plateau.PlateauService;
import com.seat.code.service.plateau.model.Plateau;

@SpringBootTest
@AutoConfigureMockMvc
class PlateausApiImplTest {

    private static final String CREATE_PLATEAU_RESOURCE_PATH = "/v1/plateaus";
    private static final String GET_PLATEAU_RESOURCE_PATH = "/v1/plateaus/{plateauId}";

    @MockBean
    private ControllerLayerMapper controllerLayerMapper;

    @MockBean
    private PlateauService plateauService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createPlateau_shouldCreatePlateau() throws Exception {
        final RectangularPlateau rectangularPlateau = buildRectangularPlateau();
        final Plateau plateau = buildPlateau();
        final UUID newPlateauId = UUID.randomUUID();

        when(controllerLayerMapper.mapToPlateau(rectangularPlateau)).thenReturn(plateau);
        when(plateauService.createPlateau(plateau)).thenReturn(newPlateauId);

        mockMvc.perform(post(CREATE_PLATEAU_RESOURCE_PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(asJsonString(rectangularPlateau)))
            .andDo(print());

        verify(controllerLayerMapper).mapToPlateau(rectangularPlateau);
        verify(plateauService).createPlateau(plateau);
        verifyNoMoreInteractions(controllerLayerMapper, plateauService);
    }

    @Test
    void createPlateau_shouldReturnCreatedHttpStatus_whenPlateauIsCreated() throws Exception {
        final RectangularPlateau rectangularPlateau = buildRectangularPlateau();
        final Plateau plateau = buildPlateau();
        final UUID newPlateauId = UUID.randomUUID();

        when(controllerLayerMapper.mapToPlateau(rectangularPlateau)).thenReturn(plateau);
        when(plateauService.createPlateau(plateau)).thenReturn(newPlateauId);

        mockMvc.perform(post(CREATE_PLATEAU_RESOURCE_PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(asJsonString(rectangularPlateau)))
            .andDo(print())
            .andExpect(status().isCreated());

        verify(controllerLayerMapper).mapToPlateau(rectangularPlateau);
        verify(plateauService).createPlateau(plateau);
        verifyNoMoreInteractions(controllerLayerMapper, plateauService);
    }

    @Test
    void createPlateau_shouldReturnLocationHeader_whenPlateauIsCreated() throws Exception {
        final RectangularPlateau rectangularPlateau = buildRectangularPlateau();
        final Plateau plateau = buildPlateau();
        final UUID newPlateauId = UUID.randomUUID();

        when(controllerLayerMapper.mapToPlateau(rectangularPlateau)).thenReturn(plateau);
        when(plateauService.createPlateau(plateau)).thenReturn(newPlateauId);

        mockMvc.perform(post(CREATE_PLATEAU_RESOURCE_PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(asJsonString(rectangularPlateau)))
            .andDo(print())
            .andExpect(header().string(LOCATION, buildPlateauLocationHeaderValue(newPlateauId)));

        verify(controllerLayerMapper).mapToPlateau(rectangularPlateau);
        verify(plateauService).createPlateau(plateau);
        verifyNoMoreInteractions(controllerLayerMapper, plateauService);
    }

    @Test
    void getPlateau_shouldReturnPlateauDetail() throws Exception {
        final UUID plateauId = UUID.randomUUID();
        final Plateau plateau = buildPlateau();
        final RectangularPlateauDetail rectangularPlateauDetail = buildRectangularPlateauDetail();

        when(plateauService.getPlateau(plateauId)).thenReturn(plateau);
        when(controllerLayerMapper.mapToPlateauDetailResponse(plateau)).thenReturn(rectangularPlateauDetail);

        mockMvc.perform(get(GET_PLATEAU_RESOURCE_PATH, plateauId)
            .accept(APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.name", equalTo(plateau.getName())))
            .andExpect(jsonPath("$.size.length", equalTo(plateau.getLength())))
            .andExpect(jsonPath("$.size.width", equalTo(plateau.getWidth())))
            .andExpect(jsonPath("$.mowers", hasSize(rectangularPlateauDetail.getMowers().size())))
            .andExpect(jsonPath("$.mowers", containsInAnyOrder(rectangularPlateauDetail.getMowers().get(0).toString(),
                rectangularPlateauDetail.getMowers().get(1).toString(),
                rectangularPlateauDetail.getMowers().get(2).toString(),
                rectangularPlateauDetail.getMowers().get(3).toString())));

        verify(plateauService).getPlateau(plateauId);
        verify(controllerLayerMapper).mapToPlateauDetailResponse(plateau);
        verifyNoMoreInteractions(plateauService, controllerLayerMapper);
    }

    private String buildPlateauLocationHeaderValue(final UUID newPlateauId) {
        return UriComponentsBuilder.fromUriString(baseURI + GET_PLATEAU_RESOURCE_PATH)
            .build(newPlateauId)
            .toString();
    }
}
