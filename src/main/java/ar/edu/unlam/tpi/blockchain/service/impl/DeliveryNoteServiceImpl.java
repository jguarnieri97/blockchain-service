package ar.edu.unlam.tpi.blockchain.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteCertifyRequestDto;
import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteVerifyRequestDto;
import ar.edu.unlam.tpi.blockchain.dto.response.BlockchainCertResponseDTO;
import ar.edu.unlam.tpi.blockchain.dto.response.MessageResponseDto;
import ar.edu.unlam.tpi.blockchain.exceptions.HashInvalidException;
import ar.edu.unlam.tpi.blockchain.exceptions.InternalException;
import ar.edu.unlam.tpi.blockchain.service.DeliveryNoteService;
import ar.edu.unlam.tpi.blockchain.service.GoogleWeb3Service;
import ar.edu.unlam.tpi.blockchain.utils.HashUtils;

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
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public MessageResponseDto verifyHash(DeliveryNoteVerifyRequestDto request) {
        try {
            String hash = HashUtils.calculateHash(request.getData());
            String hashRetrievedByBlockchain = googleWeb3Service.getHashFromTransaction(request.getTxHash());

            if (!hash.equals(hashRetrievedByBlockchain)) 
                throw new HashInvalidException("El documento y la txHash no coinciden");

            return MessageResponseDto.builder()
                    .message("El documento y la txHash coinciden")
                    .build();
            
        }
        catch (HashInvalidException e) {
            throw new HashInvalidException(e.getDetail());
        }
        catch (Exception e) {
            log.error("Error en el servidor: {}", e.getMessage(), e);
            throw new InternalException(e.getMessage());
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
