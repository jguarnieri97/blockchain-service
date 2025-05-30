package ar.edu.unlam.tpi.blockchain.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class DeliveryNoteCertifyRequestDto {


    private byte[] data;

    @JsonCreator
    public DeliveryNoteCertifyRequestDto(@JsonProperty("data") byte[] data) {
        this.data = data;
    }

}
