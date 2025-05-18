package com.example.blockchainremito.service;

import com.example.blockchainremito.dto.request.DeliveryNoteVerifyRequestDto;
import com.example.blockchainremito.dto.response.BlockchainCertResponseDTO;
import com.example.blockchainremito.dto.request.DeliveryNoteCertifyRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface DeliveryNoteService {

    @Async
    CompletableFuture<BlockchainCertResponseDTO> certifyDeliveryNote(DeliveryNoteCertifyRequestDto request);
    ResponseEntity<String> verifyHash(DeliveryNoteVerifyRequestDto request);

}
