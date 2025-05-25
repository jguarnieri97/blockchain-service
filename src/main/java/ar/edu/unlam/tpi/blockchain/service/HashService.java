package ar.edu.unlam.tpi.blockchain.service;

import ar.edu.unlam.tpi.blockchain.dto.response.MessageResponseDto;

public interface HashService {
    public String calculateHash(byte[] data);
    public MessageResponseDto checkHash(String currentHash, String hashToCompare);
}
