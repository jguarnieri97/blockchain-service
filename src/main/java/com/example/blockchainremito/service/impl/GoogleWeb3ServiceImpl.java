package com.example.blockchainremito.service.impl;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.example.blockchainremito.service.BlockchainTransactionService;
import org.springframework.stereotype.Service;
import com.example.blockchainremito.service.GoogleWeb3Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.core.methods.response.Transaction;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GoogleWeb3ServiceImpl implements GoogleWeb3Service {

    private final TransactionBuilder transactionBuilder;
    private final BlockchainTransactionService transactionService;

    @Override
    public String publishHashToBlockchain(String fromPrivateKey, String hashHex) throws Exception {
        Long chainId = 11155111L;
        return transactionBuilder.buildAndSendTransaction(fromPrivateKey, hashHex, chainId);
    }

    @Override
    public CompletableFuture<Map<String, Object>> getTxMetadata(String txHash) throws Exception {
        var tx = transactionService.getTransaction(txHash).orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
        var block = transactionService.getBlockByTransaction(tx).orElseThrow(() -> new RuntimeException("Bloque no encontrado"));

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

            if (hashRetrieved.startsWith("0x")) {
                hashRetrieved = hashRetrieved.substring(2);
            }
            return hashRetrieved.toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el hash de la transacción", e);
        }
    }

}
