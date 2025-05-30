package ar.edu.unlam.tpi.blockchain.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteCertifyRequestDto;
import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteVerifyRequestDto;
import ar.edu.unlam.tpi.blockchain.dto.response.BlockchainCertResponseDTO;
import ar.edu.unlam.tpi.blockchain.dto.response.MessageResponseDto;
import ar.edu.unlam.tpi.blockchain.exceptions.HashInvalidException;
import ar.edu.unlam.tpi.blockchain.exceptions.InternalException;
import ar.edu.unlam.tpi.blockchain.service.BlockchainTransactionService;
import ar.edu.unlam.tpi.blockchain.service.GoogleWeb3Service;
import ar.edu.unlam.tpi.blockchain.service.HashService;
import ar.edu.unlam.tpi.blockchain.utils.BlockchainCertResponseHelper;
import ar.edu.unlam.tpi.blockchain.utils.DeliveryNoteHelper;

import static ar.edu.unlam.tpi.blockchain.utils.ConstantsHelper.*;

@ExtendWith(MockitoExtension.class)
public class DeliveryNoteServiceImplTest {

    @Mock
    private GoogleWeb3Service googleWeb3Service;

    @Mock
    private HashService hashService;

    @Mock
    private BlockchainTransactionService blockchainTransactionService;

    @InjectMocks
    private DeliveryNoteServiceImpl deliveryNoteService;


    @BeforeEach
    void setUp() throws Exception{
        // Inject the private key
        ReflectionTestUtils.setField(deliveryNoteService, "privateKey", TEST_PRIVATE_KEY);

        // Setup lenient mocking for publishHashToBlockchain
        lenient().when(googleWeb3Service.publishHashToBlockchain(eq(TEST_PRIVATE_KEY), eq(HASH)))
            .thenReturn(TX_HASH);
    }

    @Test
    @DisplayName("Should successfully certify delivery note")
    void givenDeliveryNote_whenCertifyDeliveryNote_thenReturnsBlockchainCertResponse() throws Exception {
        // Arrange
        DeliveryNoteCertifyRequestDto note = DeliveryNoteHelper.createDeliveryNoteCertifyRequestDto(BODY_FILE);
        BlockchainCertResponseDTO expected = BlockchainCertResponseHelper.createBlockchainCertResponseDTO();
        Map<String, Object> txMetadata = Map.of(
            "txHash", TX_HASH,
            "blockHash", "0xBlockHash",
            "blockNumber", "123456",
            "timestamp", "2023-10-01T12:00:00Z"
        );

        when(hashService.calculateHash(note.getData()))
            .thenReturn(HASH);
        when(googleWeb3Service.getTxMetadata(TX_HASH))
            .thenReturn(CompletableFuture.completedFuture(txMetadata));
        when(blockchainTransactionService.getBlockChainCertResponse(txMetadata, HASH))
            .thenReturn(expected);

        // Act
        BlockchainCertResponseDTO response = deliveryNoteService.certifyDeliveryNote(note).join();
        
        // Assert
        assertNotNull(response);
        assertEquals(expected.getBlockNumber(), response.getBlockNumber());
        assertEquals(expected.getTxHash(), response.getTxHash());

    }

    @Test
    @DisplayName("Should throw InternalException when blockchain transaction service fails")
    void givenDeliveryNote_whenCertifyDeliveryNote_thenThrowsInternalException() throws Exception {
        // Arrange
        DeliveryNoteCertifyRequestDto note = DeliveryNoteHelper.createDeliveryNoteCertifyRequestDto(BODY_FILE);
        Map<String, Object> txMetadata = Map.of(
            "txHash", TX_HASH,
            "blockHash", "0xBlockHash",
            "blockNumber", "123456",
            "timestamp", "2023-10-01T12:00:00Z"
        );

        when(hashService.calculateHash(note.getData()))
            .thenReturn(HASH);
        when(googleWeb3Service.getTxMetadata(TX_HASH))
            .thenReturn(CompletableFuture.completedFuture(txMetadata));
        when(blockchainTransactionService.getBlockChainCertResponse(txMetadata, HASH))
            .thenThrow(new RuntimeException("Error processing blockchain response"));

        // Act & Assert
        CompletableFuture<BlockchainCertResponseDTO> future = deliveryNoteService.certifyDeliveryNote(note);
        assertThrows(CompletionException.class, () -> future.join());
    }

    @Test
    @DisplayName("Should successfully verify delivery note when hashes match")
    void givenMatchingHashes_whenVerifyDeliveryNote_thenReturnsSuccessMessage() {
        // Arrange
        DeliveryNoteVerifyRequestDto request = new DeliveryNoteVerifyRequestDto(
            BODY_FILE.getBytes(),
            TX_HASH
        );
        MessageResponseDto expectedResponse = MessageResponseDto.builder()
            .message("El documento y la txHash coinciden")
            .build();

        when(hashService.calculateHash(request.getData()))
            .thenReturn(HASH);
        when(googleWeb3Service.getHashFromTransaction(TX_HASH))
            .thenReturn(HASH);
        when(hashService.checkHash(HASH, HASH))
            .thenReturn(expectedResponse);

        // Act
        MessageResponseDto response = deliveryNoteService.verifyDeliveryNote(request);

        // Assert
        assertNotNull(response);
        assertEquals("El documento y la txHash coinciden", response.getMessage());
        verify(hashService).calculateHash(request.getData());
        verify(googleWeb3Service).getHashFromTransaction(TX_HASH);
        verify(hashService).checkHash(HASH, HASH);
    }

    @Test
    @DisplayName("Should throw HashInvalidException when hashes don't match")
    void givenNonMatchingHashes_whenVerifyDeliveryNote_thenThrowsHashInvalidException() {
        // Arrange
        DeliveryNoteVerifyRequestDto request = new DeliveryNoteVerifyRequestDto(
            BODY_FILE.getBytes(),
            TX_HASH
        );
        String differentHash = "0xDifferentHash";

        when(hashService.calculateHash(request.getData()))
            .thenReturn(HASH);
        when(googleWeb3Service.getHashFromTransaction(TX_HASH))
            .thenReturn(differentHash);
        when(hashService.checkHash(HASH, differentHash))
            .thenThrow(new HashInvalidException("El documento y la txHash no coinciden"));

        // Act & Assert
        HashInvalidException exception = assertThrows(HashInvalidException.class,
            () -> deliveryNoteService.verifyDeliveryNote(request));
        assertEquals("El documento y la txHash no coinciden", exception.getDetail());
        verify(hashService).calculateHash(request.getData());
        verify(googleWeb3Service).getHashFromTransaction(TX_HASH);
        verify(hashService).checkHash(HASH, differentHash);
    }

    @Test
    @DisplayName("Should throw InternalException when hash calculation fails")
    void givenHashCalculationError_whenVerifyDeliveryNote_thenThrowsInternalException() {
        // Arrange
        DeliveryNoteVerifyRequestDto request = new DeliveryNoteVerifyRequestDto(
            BODY_FILE.getBytes(),
            TX_HASH
        );
        String errorMessage = "Error calculating hash";

        when(hashService.calculateHash(request.getData()))
            .thenThrow(new RuntimeException(errorMessage));

        // Act & Assert
        InternalException exception = assertThrows(InternalException.class,
            () -> deliveryNoteService.verifyDeliveryNote(request));
        assertEquals(errorMessage, exception.getDetail());
        verify(hashService).calculateHash(request.getData());
        verifyNoInteractions(googleWeb3Service);
        verifyNoMoreInteractions(hashService);
    }

    @Test
    @DisplayName("Should throw InternalException when blockchain transaction fails")
    void givenBlockchainError_whenVerifyDeliveryNote_thenThrowsInternalException() {
        // Arrange
        DeliveryNoteVerifyRequestDto request = new DeliveryNoteVerifyRequestDto(
            BODY_FILE.getBytes(),
            TX_HASH
        );
        String errorMessage = "Error retrieving transaction";

        when(hashService.calculateHash(request.getData()))
            .thenReturn(HASH);
        when(googleWeb3Service.getHashFromTransaction(TX_HASH))
            .thenThrow(new RuntimeException(errorMessage));

        // Act & Assert
        InternalException exception = assertThrows(InternalException.class,
            () -> deliveryNoteService.verifyDeliveryNote(request));
        assertEquals(errorMessage, exception.getDetail());
        verify(hashService).calculateHash(request.getData());
        verify(googleWeb3Service).getHashFromTransaction(TX_HASH);
        verifyNoMoreInteractions(hashService);
    }

    @Test
    @DisplayName("Should handle null request data")
    void givenNullRequestData_whenVerifyDeliveryNote_thenThrowsException() {
        // Arrange
        DeliveryNoteVerifyRequestDto request = new DeliveryNoteVerifyRequestDto(
            null,
            TX_HASH
        );
        // Arrange
        when(hashService.calculateHash(null))
            .thenThrow(new NullPointerException("Data cannot be null"));

        // Act & Assert
        assertThrows(InternalException.class,
            () -> deliveryNoteService.verifyDeliveryNote(request));
    }

  }
