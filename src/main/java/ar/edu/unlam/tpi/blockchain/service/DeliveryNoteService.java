package ar.edu.unlam.tpi.blockchain.service;

import org.springframework.scheduling.annotation.Async;

import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteCertifyRequestDto;
import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteVerifyRequestDto;
import ar.edu.unlam.tpi.blockchain.dto.response.BlockchainCertResponseDTO;
import ar.edu.unlam.tpi.blockchain.dto.response.MessageResponseDto;

import java.util.concurrent.CompletableFuture;

public interface DeliveryNoteService {

    @Async
    CompletableFuture<BlockchainCertResponseDTO> certifyDeliveryNote(DeliveryNoteCertifyRequestDto request);
    MessageResponseDto verifyHash(DeliveryNoteVerifyRequestDto request);

}
