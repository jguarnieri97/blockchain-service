package ar.edu.unlam.tpi.blockchain.controller.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteCertifyRequestDto;
import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteVerifyRequestDto;
import ar.edu.unlam.tpi.blockchain.dto.response.BlockchainCertResponseDTO;
import ar.edu.unlam.tpi.blockchain.dto.response.MessageResponseDto;
import ar.edu.unlam.tpi.blockchain.service.DeliveryNoteService;
import ar.edu.unlam.tpi.blockchain.utils.BlockchainCertResponseHelper;
import ar.edu.unlam.tpi.blockchain.utils.DeliveryNoteHelper;

import static ar.edu.unlam.tpi.blockchain.utils.ConstantsHelper.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DeliveryNoteControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeliveryNoteService deliveryNoteService;

    private DeliveryNoteCertifyRequestDto certifyRequest;
    private DeliveryNoteVerifyRequestDto verifyRequest;
    private BlockchainCertResponseDTO mockCertResponse;
    private MessageResponseDto mockVerifyResponse;

    @BeforeEach
    void setUp() {
        // Setup certify request using helper
        certifyRequest = DeliveryNoteHelper.createDeliveryNoteCertifyRequestDto(BODY_FILE);

        // Setup verify request
        verifyRequest = new DeliveryNoteVerifyRequestDto(BODY_FILE.getBytes(), TX_HASH);

        // Setup mock responses
        mockCertResponse = BlockchainCertResponseHelper.createBlockchainCertResponseDTO();
        mockVerifyResponse = MessageResponseDto.builder()
            .message("El remito es válido y está certificado en la blockchain")
            .build();
    }

    @Test
    void givenValidDeliveryNoteData_whenCertifying_thenShouldReturnSuccessResponse() throws Exception {
        // Given
        when(deliveryNoteService.certifyDeliveryNote(any(DeliveryNoteCertifyRequestDto.class)))
            .thenReturn(CompletableFuture.completedFuture(mockCertResponse));

        // When / Then
        mockMvc.perform(post("/blockchain/v1/delivery-note")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Remito certificado correctamente"))
                .andExpect(jsonPath("$.data.txHash").value(mockCertResponse.getTxHash()))
                .andExpect(jsonPath("$.data.dataHash").value(mockCertResponse.getDataHash()))
                .andExpect(jsonPath("$.data.blockNumber").value(mockCertResponse.getBlockNumber()));
    }

    @Test
    void givenValidDeliveryNoteData_whenVerifying_thenShouldReturnSuccessResponse() throws Exception {
        // Given
        when(deliveryNoteService.verifyDeliveryNote(any(DeliveryNoteVerifyRequestDto.class)))
            .thenReturn(mockVerifyResponse);

        // When / Then
        mockMvc.perform(post("/blockchain/v1/delivery-note/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(verifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value(mockVerifyResponse.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void givenInvalidRequestBody_whenCertifying_thenShouldReturnBadRequest() throws Exception {
        // Given
        String invalidJson = "{ invalid json }";

        // When / Then
        mockMvc.perform(post("/blockchain/v1/delivery-note")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidRequestBody_whenVerifying_thenShouldReturnBadRequest() throws Exception {
        // Given
        String invalidJson = "{ invalid json }";

        // When / Then
        mockMvc.perform(post("/blockchain/v1/delivery-note/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}