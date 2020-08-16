package com.seat.code.controller;

import static com.seat.code.util.TestJsonUtils.asJsonString;
import static io.restassured.RestAssured.baseURI;
import static org.hamcrest.Matchers.equalTo;
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
import com.seat.code.controller.model.Mower;
import com.seat.code.service.MowerService;
import com.seat.code.util.TestControllerLayerObjectFactory;
import com.seat.code.util.TestServiceLayerObjectFactory;

@SpringBootTest
@AutoConfigureMockMvc
class MowersApiImplTest {

    private static final String CREATE_MOWER_RESOURCE_PATH = "/v1/plateaus/{plateauId}/mowers";
    private static final String GET_MOWER_RESOURCE_PATH = "/v1/plateaus/{plateauId}/mowers/{mowerId}";
    private static final String APPLY_MOWER_INSTRUCTIONS_RESOURCE_PATH = "/v1/plateaus/{plateauId}/mowers/{mowerId}/instructions";

    @MockBean
    private ControllerLayerMapper controllerLayerMapper;

    @MockBean
    private MowerService mowerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createMower_shouldCreateMower() throws Exception {
        final UUID plateauId = UUID.randomUUID();
        final Mower mowerRequest = TestControllerLayerObjectFactory.buildMower();
        final com.seat.code.service.model.Mower mower = TestServiceLayerObjectFactory.buildMower();
        final UUID newMowerId = UUID.randomUUID();

        when(controllerLayerMapper.mapToMower(plateauId, mowerRequest)).thenReturn(mower);
        when(mowerService.createMower(mower)).thenReturn(newMowerId);

        mockMvc.perform(post(CREATE_MOWER_RESOURCE_PATH, plateauId)
            .contentType(APPLICATION_JSON_VALUE)
            .content(asJsonString(mowerRequest)))
            .andDo(print());

        verify(controllerLayerMapper).mapToMower(plateauId, mowerRequest);
        verify(mowerService).createMower(mower);
        verifyNoMoreInteractions(controllerLayerMapper, mowerService);
    }

    @Test
    void createMower_shouldReturnCreatedHttpStatus_whenMowerIsCreated() throws Exception {
        final UUID plateauId = UUID.randomUUID();
        final Mower mowerRequest = TestControllerLayerObjectFactory.buildMower();
        final com.seat.code.service.model.Mower mower = TestServiceLayerObjectFactory.buildMower();
        final UUID newMowerId = UUID.randomUUID();

        when(controllerLayerMapper.mapToMower(plateauId, mowerRequest)).thenReturn(mower);
        when(mowerService.createMower(mower)).thenReturn(newMowerId);

        mockMvc.perform(post(CREATE_MOWER_RESOURCE_PATH, plateauId)
            .contentType(APPLICATION_JSON_VALUE)
            .content(asJsonString(mowerRequest)))
            .andDo(print())
            .andExpect(status().isCreated());

        verify(controllerLayerMapper).mapToMower(plateauId, mowerRequest);
        verify(mowerService).createMower(mower);
        verifyNoMoreInteractions(controllerLayerMapper, mowerService);
    }

    @Test
    void createMower_shouldReturnLocationHeader_whenMowerIsCreated() throws Exception {
        final UUID plateauId = UUID.randomUUID();
        final Mower mowerRequest = TestControllerLayerObjectFactory.buildMower();
        final com.seat.code.service.model.Mower mower = TestServiceLayerObjectFactory.buildMower();
        final UUID newMowerId = UUID.randomUUID();

        when(controllerLayerMapper.mapToMower(plateauId, mowerRequest)).thenReturn(mower);
        when(mowerService.createMower(mower)).thenReturn(newMowerId);

        mockMvc.perform(post(CREATE_MOWER_RESOURCE_PATH, plateauId)
            .contentType(APPLICATION_JSON_VALUE)
            .content(asJsonString(mowerRequest)))
            .andDo(print())
            .andExpect(header().string(LOCATION, buildMowerLocationHeaderValue(plateauId, newMowerId)));

        verify(controllerLayerMapper).mapToMower(plateauId, mowerRequest);
        verify(mowerService).createMower(mower);
        verifyNoMoreInteractions(controllerLayerMapper, mowerService);
    }

    @Test
    void getMower_shouldReturnMowerPosition() throws Exception {
        final UUID plateauId = UUID.randomUUID();
        final UUID mowerId = UUID.randomUUID();
        final com.seat.code.service.model.Mower mower = TestServiceLayerObjectFactory.buildMower();
        final Mower mowerResponse = TestControllerLayerObjectFactory.buildMower();

        when(mowerService.getMower(plateauId, mowerId)).thenReturn(mower);
        when(controllerLayerMapper.mapToMowerResponse(mower)).thenReturn(mowerResponse);

        mockMvc.perform(get(GET_MOWER_RESOURCE_PATH, plateauId, mowerId)
            .contentType(APPLICATION_JSON_VALUE)
            .accept(APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.name", equalTo(mowerResponse.getName())))
            .andExpect(jsonPath("$.position.latitude", equalTo(mowerResponse.getPosition().getLatitude())))
            .andExpect(jsonPath("$.position.longitude", equalTo(mowerResponse.getPosition().getLongitude())))
            .andExpect(jsonPath("$.position.orientation", equalTo(mowerResponse.getPosition().getOrientation().getValue())));

        verify(mowerService).getMower(plateauId, mowerId);
        verify(controllerLayerMapper).mapToMowerResponse(mower);
        verifyNoMoreInteractions(mowerService, controllerLayerMapper);
    }

    private String buildMowerLocationHeaderValue(final UUID plateauId, final UUID newMowerId) {
        return UriComponentsBuilder.fromUriString(baseURI + GET_MOWER_RESOURCE_PATH)
            .build(plateauId, newMowerId)
            .toString();
    }
}
