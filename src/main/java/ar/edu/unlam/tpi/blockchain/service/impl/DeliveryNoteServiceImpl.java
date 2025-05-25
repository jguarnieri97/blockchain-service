package ar.edu.unlam.tpi.blockchain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteCertifyRequestDto;
import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteVerifyRequestDto;
import ar.edu.unlam.tpi.blockchain.dto.response.BlockchainCertResponseDTO;
import ar.edu.unlam.tpi.blockchain.dto.response.MessageResponseDto;
import ar.edu.unlam.tpi.blockchain.exceptions.HashInvalidException;
import ar.edu.unlam.tpi.blockchain.exceptions.InternalException;
import ar.edu.unlam.tpi.blockchain.service.BlockchainTransactionService;
import ar.edu.unlam.tpi.blockchain.service.DeliveryNoteService;
import ar.edu.unlam.tpi.blockchain.service.GoogleWeb3Service;
import ar.edu.unlam.tpi.blockchain.service.HashService;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryNoteServiceImpl implements DeliveryNoteService {

    private final GoogleWeb3Service googleWeb3Service;    

    private final HashService hashService;
    
    private final BlockchainTransactionService blockchainTransactionService;

    private final String privateKey;

    @Async
    @Override
    public CompletableFuture<BlockchainCertResponseDTO> certifyDeliveryNote(DeliveryNoteCertifyRequestDto request) {
        try{
            String hash = hashService.calculateHash(request.getData());
            String txHash = googleWeb3Service.publishHashToBlockchain(privateKey, hash);
            return googleWeb3Service.getTxMetadata(txHash)
                    .thenApply(txMetadata -> blockchainTransactionService.getBlockChainCertResponse(txMetadata, hash));
        } catch (Exception e) {
            log.error("Error al certificar el remito: {}", e.getMessage(), e);
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public MessageResponseDto verifyDeliveryNote(DeliveryNoteVerifyRequestDto request) {
        try {
            String hash = hashService.calculateHash(request.getData());
            String hashRetrievedByBlockchain = googleWeb3Service.getHashFromTransaction(request.getTxHash());
            return hashService.checkHash(hash, hashRetrievedByBlockchain);
        }
        catch (HashInvalidException e) {
            throw new HashInvalidException(e.getDetail());
        }
        catch (Exception e) {
            log.error("Error en el servidor: {}", e.getMessage(), e);
            throw new InternalException(e.getMessage());
        }
    }



}
