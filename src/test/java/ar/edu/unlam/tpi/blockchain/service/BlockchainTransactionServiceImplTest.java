package ar.edu.unlam.tpi.blockchain.service;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigInteger;
import java.util.Map;
import ar.edu.unlam.tpi.blockchain.dto.response.BlockchainCertResponseDTO;
import ar.edu.unlam.tpi.blockchain.service.impl.BlockchainTransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

@ExtendWith(MockitoExtension.class)
public class BlockchainTransactionServiceImplTest {

    @Mock
    private Web3j web3j;

    @InjectMocks
    private BlockchainTransactionServiceImpl service;

    private Credentials credentials;
    private static final String TEST_PRIVATE_KEY = "0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef";

    @BeforeEach
    void setUp() {
        credentials = Credentials.create(TEST_PRIVATE_KEY);
    }

    @Test
    @DisplayName("Should throw exception if sending transaction fails")
    void sendTransaction_WithError_ShouldThrowException() throws Exception {
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
    @DisplayName("Should build blockchain cert response")
    void getBlockChainCertResponse_ShouldBuildResponse() {
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

