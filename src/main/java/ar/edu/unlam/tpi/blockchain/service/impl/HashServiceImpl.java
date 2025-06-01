package ar.edu.unlam.tpi.blockchain.service.impl;

import org.springframework.stereotype.Service;

import ar.edu.unlam.tpi.blockchain.dto.response.MessageResponseDto;
import ar.edu.unlam.tpi.blockchain.exceptions.HashInvalidException;
import ar.edu.unlam.tpi.blockchain.service.HashService;
import lombok.extern.slf4j.Slf4j;
import jakarta.xml.bind.DatatypeConverter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class HashServiceImpl implements HashService{
    
    @Override
    public String calculateHash(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            return DatatypeConverter.printHexBinary(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No se pudo generar el hash", e);
        }
    }

    @Override
    public MessageResponseDto checkHash(String currentHash, String hashToCompare) {
        if (!currentHash.equals(hashToCompare)) 
            throw new HashInvalidException("El documento y la txHash no coinciden");

        return MessageResponseDto.builder()
                .message("El documento y la txHash coinciden")
                .build();
    }
}
