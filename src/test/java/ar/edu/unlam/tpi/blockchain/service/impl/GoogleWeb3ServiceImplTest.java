package ar.edu.unlam.tpi.blockchain.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.Transaction;

import ar.edu.unlam.tpi.blockchain.service.BlockchainTransactionService;

import static ar.edu.unlam.tpi.blockchain.utils.ConstantsHelper.*;

@ExtendWith(MockitoExtension.class)
public class GoogleWeb3ServiceImplTest {

    @Mock
    private BlockchainTransactionService transactionService;

    @InjectMocks
    private GoogleWeb3ServiceImpl googleWeb3Service;

    @Test
    @DisplayName("Should successfully publish hash to blockchain")
    void givenValidPrivateKeyAndHash_whenPublishHashToBlockchain_thenReturnsTxHash() throws Exception {
        RawTransaction mockTransaction = mock(RawTransaction.class);
        String expectedTxHash = "0x" + TX_HASH;

        when(transactionService.buildTransaction(any(Credentials.class), eq(HASH)))
            .thenReturn(mockTransaction);
        when(transactionService.sendTransaction(any(Credentials.class), eq(TEST_CHAIN_ID), eq(mockTransaction)))
            .thenReturn(expectedTxHash);

        String result = googleWeb3Service.publishHashToBlockchain(TEST_PRIVATE_KEY, HASH);

        assertEquals(expectedTxHash, result);
        verify(transactionService).buildTransaction(any(Credentials.class), eq(HASH));
        verify(transactionService).sendTransaction(any(Credentials.class), eq(TEST_CHAIN_ID), eq(mockTransaction));
    }

    @Test
    @DisplayName("Should throw exception when transaction service fails")
    void givenServiceFailure_whenPublishHashToBlockchain_thenThrowsException() throws Exception {
        when(transactionService.buildTransaction(any(Credentials.class), any()))
            .thenThrow(new RuntimeException("Service error"));

        assertThrows(RuntimeException.class,
            () -> googleWeb3Service.publishHashToBlockchain(TEST_PRIVATE_KEY, HASH));
    }

    @Test
    @DisplayName("Should successfully get transaction metadata")
    void givenValidTxHash_whenGetTxMetadata_thenReturnsMetadataMap() throws Exception {
        Transaction mockTx = mock(Transaction.class);
        Block mockBlock = mock(Block.class);
        BigInteger blockNumber = BigInteger.valueOf(12345);
        BigInteger timestamp = BigInteger.valueOf(Instant.now().getEpochSecond());

        when(transactionService.getTransaction(TX_HASH))
            .thenReturn(Optional.of(mockTx));
        when(transactionService.getBlockByTransaction(mockTx))
            .thenReturn(Optional.of(mockBlock));
        when(mockBlock.getNumber()).thenReturn(blockNumber);
        when(mockBlock.getTimestamp()).thenReturn(timestamp);

        CompletableFuture<Map<String, Object>> future = googleWeb3Service.getTxMetadata(TX_HASH);
        Map<String, Object> result = future.get();

        assertNotNull(result);
        assertEquals(TX_HASH, result.get("txHash"));
        assertEquals(blockNumber, result.get("blockNumber"));
        assertEquals(Instant.ofEpochSecond(timestamp.longValue()), result.get("timestamp"));
    }

    @Test
    @DisplayName("Should throw exception when transaction not found")
    void givenMissingTransaction_whenGetTxMetadata_thenThrowsException() throws Exception {
        when(transactionService.getTransaction(TX_HASH))
            .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
            () -> googleWeb3Service.getTxMetadata(TX_HASH));
    }

    @Test
    @DisplayName("Should successfully get hash from transaction")
    void givenTransactionWithInputHash_whenGetHashFromTransaction_thenReturnsHashWithoutPrefix() throws Exception {
        Transaction mockTx = mock(Transaction.class);
        String inputHash = HASH;

        when(transactionService.getTransaction(TX_HASH))
            .thenReturn(Optional.of(mockTx));
        when(mockTx.getInput()).thenReturn(inputHash);

        String result = googleWeb3Service.getHashFromTransaction(TX_HASH);

        assertEquals(HASH.substring(2).toUpperCase(), result);
    }

    @Test
    @DisplayName("Should handle hash without 0x prefix")
    void givenTransactionWithRawHash_whenGetHashFromTransaction_thenReturnsHashAsIs() throws Exception {
        Transaction mockTx = mock(Transaction.class);
        String inputHash = HASH.substring(2);

        when(transactionService.getTransaction(TX_HASH))
            .thenReturn(Optional.of(mockTx));
        when(mockTx.getInput()).thenReturn(inputHash);

        String result = googleWeb3Service.getHashFromTransaction(TX_HASH);

        assertEquals(HASH.substring(2).toUpperCase(), result);
    }

    @Test
    @DisplayName("Should throw exception when transaction not found in getHashFromTransaction")
    void givenMissingTransaction_whenGetHashFromTransaction_thenThrowsException() throws Exception {
        when(transactionService.getTransaction(TX_HASH))
            .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> googleWeb3Service.getHashFromTransaction(TX_HASH));
        assertEquals("Error al obtener el hash de la transacción", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when transaction service fails in getHashFromTransaction")
    void givenServiceFailure_whenGetHashFromTransaction_thenThrowsException() throws Exception {
        when(transactionService.getTransaction(TX_HASH))
            .thenThrow(new RuntimeException("Service error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> googleWeb3Service.getHashFromTransaction(TX_HASH));
        assertEquals("Error al obtener el hash de la transacción", exception.getMessage());
    }
}
