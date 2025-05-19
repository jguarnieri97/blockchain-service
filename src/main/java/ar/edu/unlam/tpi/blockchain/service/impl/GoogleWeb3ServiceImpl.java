package ar.edu.unlam.tpi.blockchain.service.impl;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock.Block;

import ar.edu.unlam.tpi.blockchain.service.BlockchainTransactionService;
import ar.edu.unlam.tpi.blockchain.service.GoogleWeb3Service;
import ar.edu.unlam.tpi.blockchain.service.TransactionBuilderService;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GoogleWeb3ServiceImpl implements GoogleWeb3Service {

    private final TransactionBuilderService transactionBuilder;
    private final BlockchainTransactionService transactionService;

    @Override
    public String publishHashToBlockchain(String fromPrivateKey, String hashHex) throws Exception {
        Long chainId = 11155111L;
        return transactionBuilder.buildAndSendTransaction(fromPrivateKey, hashHex, chainId);
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

            if (hashRetrieved.startsWith("0x")) {
                hashRetrieved = hashRetrieved.substring(2);
            }
            return hashRetrieved.toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el hash de la transacción", e);
        }
    }

}
