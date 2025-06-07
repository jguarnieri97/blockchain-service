package ar.edu.unlam.tpi.blockchain.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.unlam.tpi.blockchain.dto.response.MessageResponseDto;
import ar.edu.unlam.tpi.blockchain.exceptions.HashInvalidException;

@ExtendWith(MockitoExtension.class)
class HashServiceImplTest {

    @InjectMocks
    private HashServiceImpl hashService;

    @Test
    void givenValidInput_whenCalculateHash_thenReturnsExpectedHash() {
        byte[] inputData = "0x4D7953514C4C6F6E67426C6F624578616D706C6544617461417348657844617461323032342D30352D31385465737444617461426C6F62".getBytes();
        String expectedHash = "36831A481FF977E998DC43DEDD3EDF0D3DDEFA28C58DEBDFFB059761D774BDE9";

        String actualHash = hashService.calculateHash(inputData);

        assertEquals(expectedHash, actualHash);
    }

    @Test
    void givenMatchingHashes_whenCheckHash_thenReturnsSuccessMessage() {
        String hash = "36831A481FF977E998DC43DEDD3EDF0D3DDEFA28C58DEBDFFB059761D774BDE9";

        MessageResponseDto response = hashService.checkHash(hash, hash);

        assertNotNull(response);
        assertEquals("El documento y la txHash coinciden", response.getMessage());
    }

    @Test
    void givenNonMatchingHashes_whenCheckHash_thenThrowsHashInvalidException() {
        String hash1 = "916F0027A575074CE72A331777C3478D6513F786A591BD892DA1A577BF2335F9";
        String hash2 = "DIFFERENT_HASH";

        HashInvalidException exception = assertThrows(HashInvalidException.class,
            () -> hashService.checkHash(hash1, hash2));

        assertEquals("El documento y la txHash no coinciden", exception.getDetail());
    }

    @Test
    void givenEmptyData_whenCalculateHash_thenReturnsValidHash() {
        byte[] emptyData = new byte[0];
        String expectedHash = "E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855";

        String actualHash = hashService.calculateHash(emptyData);

        assertEquals(expectedHash, actualHash);
    }

    @Test
    void givenNullData_whenCalculateHash_thenThrowsNullPointerException() {
        byte[] nullData = null;

        assertThrows(NullPointerException.class, () -> hashService.calculateHash(nullData));
    }
}
