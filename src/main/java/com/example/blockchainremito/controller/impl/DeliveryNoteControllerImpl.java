package com.example.blockchainremito.controller.impl;

import com.example.blockchainremito.controller.DeliveryNoteController;
import com.example.blockchainremito.dto.request.DeliveryNoteVerifyRequestDto;
import com.example.blockchainremito.dto.response.BlockchainCertResponseDTO;
import com.example.blockchainremito.dto.request.DeliveryNoteCertifyRequestDto;
import com.example.blockchainremito.dto.response.GenericResponseDto;
import com.example.blockchainremito.service.DeliveryNoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DeliveryNoteControllerImpl implements DeliveryNoteController {

    private final DeliveryNoteService deliveryNoteService;

    @Override
    public GenericResponseDto<BlockchainCertResponseDTO> certifyDeliveryNote(DeliveryNoteCertifyRequestDto request) {
        BlockchainCertResponseDTO response = deliveryNoteService.certifyDeliveryNote(request).join();
        return new GenericResponseDto<>(
                200,
                "Remito certificado correctamente",
                response);
    }

    @Override
    public GenericResponseDto<Object> verifyDeliveryNote(DeliveryNoteVerifyRequestDto request) {
        ResponseEntity<String> response = deliveryNoteService.verifyHash(request);
        return new GenericResponseDto<>(
                response.getStatusCodeValue(),
                response.getBody(),
                null
        );
    }

}