package com.example.blockchainremito.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlockchainCertResponseDTO {

    private String txHash;
    private String dataHash;
    private Integer blockNumber;

}
