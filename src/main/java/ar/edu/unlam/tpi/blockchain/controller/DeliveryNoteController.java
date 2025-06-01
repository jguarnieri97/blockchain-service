package ar.edu.unlam.tpi.blockchain.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ar.edu.unlam.tpi.blockchain.dto.response.GenericResponseDto;
import ar.edu.unlam.tpi.blockchain.dto.response.MessageResponseDto;
import ar.edu.unlam.tpi.blockchain.dto.response.BlockchainCertResponseDTO;
import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteCertifyRequestDto;
import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteVerifyRequestDto;

@RequestMapping("blockchain/v1/delivery-note")
public interface DeliveryNoteController {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Certificar un remito")
    GenericResponseDto<BlockchainCertResponseDTO> certifyDeliveryNote(
            @Valid @RequestBody DeliveryNoteCertifyRequestDto request);

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Verificar un remito")
    GenericResponseDto<MessageResponseDto> verifyDeliveryNote(
            @Valid @RequestBody DeliveryNoteVerifyRequestDto request);
}
