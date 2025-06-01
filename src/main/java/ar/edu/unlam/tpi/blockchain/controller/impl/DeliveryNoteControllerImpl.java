package ar.edu.unlam.tpi.blockchain.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.unlam.tpi.blockchain.controller.DeliveryNoteController;
import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteCertifyRequestDto;
import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteVerifyRequestDto;
import ar.edu.unlam.tpi.blockchain.dto.response.BlockchainCertResponseDTO;
import ar.edu.unlam.tpi.blockchain.dto.response.GenericResponseDto;
import ar.edu.unlam.tpi.blockchain.dto.response.MessageResponseDto;
import ar.edu.unlam.tpi.blockchain.service.DeliveryNoteService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Delivery Note", description = "Controller relacionadas a remitos")
public class DeliveryNoteControllerImpl implements DeliveryNoteController {

    private final DeliveryNoteService deliveryNoteService;

    @Override
    public GenericResponseDto<BlockchainCertResponseDTO> certifyDeliveryNote(DeliveryNoteCertifyRequestDto request) {
        BlockchainCertResponseDTO response = deliveryNoteService.certifyDeliveryNote(request).join();
        return new GenericResponseDto<>(
                HttpStatus.OK.value(),
                "Remito certificado correctamente",
                response);
    }

    @Override
    public GenericResponseDto<MessageResponseDto> verifyDeliveryNote(DeliveryNoteVerifyRequestDto request) {
        MessageResponseDto response = deliveryNoteService.verifyDeliveryNote(request);
        return new GenericResponseDto<>(
                HttpStatus.OK.value(),    
                response.getMessage(),
                null
        );
    }

}