package com.example.blockchainremito.controller.impl;

import com.example.blockchainremito.controller.DeliveryNoteController;
import com.example.blockchainremito.dto.DeliveryNoteDto;
import com.example.blockchainremito.dto.GenericResponse;
import com.example.blockchainremito.service.DeliveryNoteService;
import com.example.blockchainremito.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DeliveryNoteControllerImpl implements DeliveryNoteController {

    private final DeliveryNoteService deliveryNoteService;

    @Override
    public GenericResponse<DeliveryNoteDto> getDeliveryNoteById(Long deliveryNoteId) {
        DeliveryNoteDto deliveryNote = deliveryNoteService.getDeliveryNoteById(deliveryNoteId);
        return new GenericResponse<>(
                Constants.STATUS_OK,
                Constants.SUCCESS_MESSAGE,
                deliveryNote);
    }

    @Override
    public ResponseEntity<byte[]> downloadDeliveryNote(Long id) {
        DeliveryNoteDto dto = this.deliveryNoteService.getDeliveryNoteById(id);
        
        return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=remito.pdf")
        .body(dto.getData());
    }
}