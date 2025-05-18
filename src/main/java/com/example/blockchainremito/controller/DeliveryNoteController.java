package com.example.blockchainremito.controller;

import com.example.blockchainremito.dto.request.DeliveryNoteVerifyRequestDto;
import com.example.blockchainremito.dto.response.BlockchainCertResponseDTO;
import com.example.blockchainremito.dto.request.DeliveryNoteCertifyRequestDto;
import com.example.blockchainremito.dto.response.GenericResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("blockchain/v1/delivery-note")
@Validated
public interface DeliveryNoteController {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Certify a delivery note")
    GenericResponseDto<BlockchainCertResponseDTO> certifyDeliveryNote(
            @Valid @RequestBody DeliveryNoteCertifyRequestDto request);

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Verify a delivery note")
    GenericResponseDto<Object> verifyDeliveryNote(
            @Valid @RequestBody DeliveryNoteVerifyRequestDto request);

//    @GetMapping()
//    @ResponseStatus(HttpStatus.OK)
//    GenericResponse<DeliveryNoteDto> getDeliveryNoteById(@RequestParam(required = false) Long deliveryNoteId);
//
//    @GetMapping("/download/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    ResponseEntity<byte[]> downloadDeliveryNote(@PathVariable Long id);
}
