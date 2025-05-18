package com.example.blockchainremito.service.impl;

import com.example.blockchainremito.dto.request.DeliveryNoteVerifyRequestDto;
import com.example.blockchainremito.dto.response.BlockchainCertResponseDTO;
import com.example.blockchainremito.dto.request.DeliveryNoteCertifyRequestDto;
import com.example.blockchainremito.service.DeliveryNoteService;

import com.example.blockchainremito.service.GoogleWeb3Service;
import com.example.blockchainremito.utils.HashUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DeliveryNoteServiceImpl implements DeliveryNoteService {

    private static final String BLOCK_NUMBER_KEY = "blockNumber";

    GoogleWeb3Service googleWeb3Service;

    @Qualifier("privateKey")
    String privateKey;

    @Async
    @Override
    public CompletableFuture<BlockchainCertResponseDTO> certifyDeliveryNote(DeliveryNoteCertifyRequestDto request) {
        try{
            String hash = HashUtils.calculateHash(request.getData());

            String txHash = googleWeb3Service.publishHashToBlockchain(privateKey, hash);

            return googleWeb3Service.getTxMetadata(txHash)
                    .thenApply(txMetadata -> buildBlockchainCertResponse(txMetadata, hash));
        } catch (Exception e) {
            log.error("Error al certificar el remito: {}", e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public ResponseEntity<String> verifyHash(DeliveryNoteVerifyRequestDto request) {
        try {
            String hash = HashUtils.calculateHash(request.getData());
            String hashRetrievedByBlockchain = googleWeb3Service.getHashFromTransaction(request.getTxHash());

            if (hash.equals(hashRetrievedByBlockchain)) {
                return ResponseEntity.status(HttpStatus.OK).body("Success");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El documento fue adulterado");
            }
        } catch (Exception e) {
            log.error("Error al verificar el hash: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    private BlockchainCertResponseDTO buildBlockchainCertResponse(Map<String, Object> txMetadata, String hash) {
        Integer blockNumber = ((BigInteger) txMetadata.get(BLOCK_NUMBER_KEY)).intValue();

        return BlockchainCertResponseDTO.builder()
                .txHash((String) txMetadata.get("txHash"))
                .dataHash(hash)
                .blockNumber(blockNumber)
                .build();
    }

}
