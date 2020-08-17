package com.seat.code.controller;

import static com.seat.code.controller.model.MowerInstruction.MOVE_FORWARD;
import static com.seat.code.controller.model.MowerInstruction.SPIN_LEFT;
import static com.seat.code.controller.model.MowerInstruction.SPIN_RIGHT;
import static com.seat.code.util.TestJsonUtils.asJsonString;
import static io.restassured.RestAssured.baseURI;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;
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

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import com.seat.code.controller.model.Mower;
import com.seat.code.controller.model.MowerInstruction;
import com.seat.code.service.mower.MowerService;
import com.seat.code.service.mower.instruction.MowerInstructionService;
import com.seat.code.util.TestControllerLayerObjectFactory;
import com.seat.code.util.TestServiceLayerObjectFactory;

@WebMvcTest(MowersApiImpl.class)
class MowersApiImplTest {

    private static final String CREATE_MOWER_RESOURCE_PATH = "/v1/plateaus/{plateauId}/mowers";
    private static final String GET_MOWER_RESOURCE_PATH = "/v1/plateaus/{plateauId}/mowers/{mowerId}";
    private static final String APPLY_MOWER_INSTRUCTIONS_RESOURCE_PATH = "/v1/plateaus/{plateauId}/mowers/{mowerId}/instructions";

    // if we use @MockBean, FormattingConversionService bean not found exception is appearing and so far there is no way to get rid of that
    @SpyBean
    private ConversionService conversionService;

    @MockBean
    private MowerService mowerService;

    @MockBean
    private MowerInstructionService mowerInstructionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createMower_shouldCreateMower() throws Exception {
        final UUID plateauId = UUID.randomUUID();
        final Mower mowerRequest = TestControllerLayerObjectFactory.buildMower();
        final com.seat.code.service.mower.model.Mower mower = TestServiceLayerObjectFactory.buildMower();
        final UUID newMowerId = UUID.randomUUID();

        doReturn(mower).when(conversionService).convert(mowerRequest, com.seat.code.service.mower.model.Mower.class);
        when(mowerService.createMower(plateauId, mower)).thenReturn(newMowerId);

        mockMvc.perform(post(CREATE_MOWER_RESOURCE_PATH, plateauId)
            .contentType(APPLICATION_JSON_VALUE)
            .content(asJsonString(mowerRequest)))
            .andDo(print());

        verify(conversionService).convert(mowerRequest, com.seat.code.service.mower.model.Mower.class);
        verify(mowerService).createMower(plateauId, mower);
        verifyNoMoreInteractions(mowerService);
    }

    @Test
    void createMower_shouldReturnCreatedHttpStatus_whenMowerIsCreated() throws Exception {
        final UUID plateauId = UUID.randomUUID();
        final Mower mowerRequest = TestControllerLayerObjectFactory.buildMower();
        final com.seat.code.service.mower.model.Mower mower = TestServiceLayerObjectFactory.buildMower();
        final UUID newMowerId = UUID.randomUUID();

        doReturn(mower).when(conversionService).convert(mowerRequest, com.seat.code.service.mower.model.Mower.class);
        when(mowerService.createMower(plateauId, mower)).thenReturn(newMowerId);

        mockMvc.perform(post(CREATE_MOWER_RESOURCE_PATH, plateauId)
            .contentType(APPLICATION_JSON_VALUE)
            .content(asJsonString(mowerRequest)))
            .andDo(print())
            .andExpect(status().isCreated());

        verify(conversionService).convert(mowerRequest, com.seat.code.service.mower.model.Mower.class);
        verify(mowerService).createMower(plateauId, mower);
        verifyNoMoreInteractions(mowerService);
    }

    @Test
    void createMower_shouldReturnLocationHeader_whenMowerIsCreated() throws Exception {
        final UUID plateauId = UUID.randomUUID();
        final Mower mowerRequest = TestControllerLayerObjectFactory.buildMower();
        final com.seat.code.service.mower.model.Mower mower = TestServiceLayerObjectFactory.buildMower();
        final UUID newMowerId = UUID.randomUUID();

        doReturn(mower).when(conversionService).convert(mowerRequest, com.seat.code.service.mower.model.Mower.class);
        when(mowerService.createMower(plateauId, mower)).thenReturn(newMowerId);

        mockMvc.perform(post(CREATE_MOWER_RESOURCE_PATH, plateauId)
            .contentType(APPLICATION_JSON_VALUE)
            .content(asJsonString(mowerRequest)))
            .andDo(print())
            .andExpect(header().string(LOCATION, buildMowerLocationHeaderValue(plateauId, newMowerId)));

        verify(conversionService).convert(mowerRequest, com.seat.code.service.mower.model.Mower.class);
        verify(mowerService).createMower(plateauId, mower);
        verifyNoMoreInteractions(mowerService);
    }

    @Test
    void getMower_shouldReturnMowerPosition() throws Exception {
        final UUID plateauId = UUID.randomUUID();
        final UUID mowerId = UUID.randomUUID();
        final com.seat.code.service.mower.model.Mower mower = TestServiceLayerObjectFactory.buildMower();
        final Mower mowerResponse = TestControllerLayerObjectFactory.buildMower();

        when(mowerService.getMower(plateauId, mowerId)).thenReturn(mower);
        doReturn(mowerResponse).when(conversionService).convert(mower, Mower.class);

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
        verify(conversionService).convert(mower, Mower.class);
        verifyNoMoreInteractions(mowerService);
    }

    @Test
    void applyMowerInstructions_shouldApplyInstructionsToTargetingMowerAndReturnNoContentHttpStatus() throws Exception {
        final UUID plateauId = UUID.randomUUID();
        final UUID mowerId = UUID.randomUUID();
        final List<MowerInstruction> mowerInstructionsRequest = asList(SPIN_LEFT, SPIN_RIGHT, MOVE_FORWARD);
        final List<com.seat.code.service.mower.model.MowerInstruction> mowerInstructions = asList(com.seat.code.service.mower.model.MowerInstruction.SPIN_LEFT,
            com.seat.code.service.mower.model.MowerInstruction.SPIN_RIGHT,
            com.seat.code.service.mower.model.MowerInstruction.MOVE_FORWARD);

        for (int index = 0; index < mowerInstructionsRequest.size(); index++) {
            doReturn(mowerInstructions.get(index))
                .when(conversionService)
                .convert(mowerInstructionsRequest.get(index), com.seat.code.service.mower.model.MowerInstruction.class);
        }

        mockMvc.perform(post(APPLY_MOWER_INSTRUCTIONS_RESOURCE_PATH, plateauId, mowerId)
            .contentType(APPLICATION_JSON_VALUE)
            .content(asJsonString(mowerInstructionsRequest)))
            .andDo(print())
            .andExpect(status().isNoContent());

        mowerInstructionsRequest.forEach(mowerInstructionRequest -> verify(conversionService)
            .convert(mowerInstructionRequest, com.seat.code.service.mower.model.MowerInstruction.class));
        verify(mowerInstructionService).applyInstructionsToMower(mowerInstructions, plateauId, mowerId);
        verifyNoMoreInteractions(mowerInstructionService);
    }

    private String buildMowerLocationHeaderValue(final UUID plateauId, final UUID newMowerId) {
        return UriComponentsBuilder.fromUriString(baseURI + GET_MOWER_RESOURCE_PATH)
            .build(plateauId, newMowerId)
            .toString();
    }
}
