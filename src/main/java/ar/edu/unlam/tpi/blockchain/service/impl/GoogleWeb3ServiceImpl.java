package ar.edu.unlam.tpi.blockchain.service.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import ar.edu.unlam.tpi.blockchain.service.BlockchainTransactionService;
import ar.edu.unlam.tpi.blockchain.service.GoogleWeb3Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleWeb3ServiceImpl implements GoogleWeb3Service {

    private final BlockchainTransactionService transactionService;

    @Override
    public String publishHashToBlockchain(String fromPrivateKey, String hashHex) throws Exception {
        final Long CHAIN_ID = 11155111L;
        Credentials credential =  Credentials.create(fromPrivateKey);
        RawTransaction transactionCreated = transactionService.buildTransaction(credential, hashHex);
        return transactionService.sendTransaction(credential, CHAIN_ID, transactionCreated);
    }

    @Override
    public CompletableFuture<Map<String, Object>> getTxMetadata(String txHash) throws Exception {
        Transaction tx = transactionService.getTransaction(txHash).orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
        Block block = transactionService.getBlockByTransaction(tx).orElseThrow(() -> new RuntimeException("Bloque no encontrado"));

        Map<String, Object> result = new HashMap<>();
        result.put("txHash", txHash);
        result.put("blockNumber", block.getNumber());
        result.put("timestamp", Instant.ofEpochSecond(block.getTimestamp().longValue()));
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public String getHashFromTransaction(String txHash) {
        try {
            Transaction transactionResponse = transactionService.getTransaction(txHash).orElseThrow();
            String hashRetrieved = transactionResponse.getInput();
            if (hashRetrieved.startsWith("0x"))
                hashRetrieved = hashRetrieved.substring(2);
            
            return hashRetrieved.toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el hash de la transacción", e);
        }
    }

}
