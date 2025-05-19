package ar.edu.unlam.tpi.blockchain.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeliveryNoteVerifyRequestDto {

    private byte[] data;
    private String txHash;

    @JsonCreator
    public DeliveryNoteVerifyRequestDto(
            @JsonProperty("data") byte[] data,
            @JsonProperty("txHash") String txHash) {
        this.data = data;
        this.txHash = txHash;
    }
}
