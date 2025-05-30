package ar.edu.unlam.tpi.blockchain.utils;

import ar.edu.unlam.tpi.blockchain.dto.response.BlockchainCertResponseDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BlockchainCertResponseHelper {
    
    public BlockchainCertResponseDTO createBlockchainCertResponseDTO(){
        return BlockchainCertResponseDTO.builder()
                .txHash("0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef")
                .dataHash("0xabcdef1234567890abcdef1234567890abcdef1234567890abcdef1234567890")
                .blockNumber(123456)
                .build(); 
    }
}
