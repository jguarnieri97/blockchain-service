package ar.edu.unlam.tpi.blockchain.service.impl;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigInteger;
import java.util.Map;
import ar.edu.unlam.tpi.blockchain.dto.response.BlockchainCertResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.*;

import static ar.edu.unlam.tpi.blockchain.utils.ConstantsHelper.*;

@ExtendWith(MockitoExtension.class)
public class BlockchainTransactionServiceImplTest {

    @Mock
    private Web3j web3j;

    @InjectMocks
    private BlockchainTransactionServiceImpl service;

    private Credentials credentials;

    @BeforeEach
    void setUp() {
        credentials = Credentials.create(TEST_PRIVATE_KEY);
    }

    @Test
    void givenTransactionWithError_whenSending_thenShouldThrowException() throws Exception {
        // Given
        RawTransaction tx = RawTransaction.createTransaction(
            BigInteger.ONE, 
            BigInteger.TEN, 
            BigInteger.valueOf(21000), 
            credentials.getAddress(), 
            BigInteger.ZERO, 
            "0xABCDEF"
        );

        EthSendTransaction mockSend = new EthSendTransaction();
        Response.Error error = new Response.Error(1, "Error interno");
        mockSend.setError(error);
        
        // When / Then
        Exception exception = assertThrows(RuntimeException.class, 
            () -> service.sendTransaction(credentials, 11155111L, tx));
        assertFalse(exception.getMessage().contains("Error al enviar tx"));
    }

    @Test
    void givenMetadataAndHash_whenBuildingResponse_thenShouldReturnBlockchainCert() {
        // Given
        Map<String, Object> metadata = Map.of(
            "txHash", "0xTXHASH",
            "blockNumber", BigInteger.valueOf(12345)
        );
        String hash = "0xDATAHASH";

        // When
        BlockchainCertResponseDTO response = service.getBlockChainCertResponse(metadata, hash);

        // Then
        assertNotNull(response);
        assertEquals("0xTXHASH", response.getTxHash());
        assertEquals(hash, response.getDataHash());
        assertEquals(12345, response.getBlockNumber());
    }
}

