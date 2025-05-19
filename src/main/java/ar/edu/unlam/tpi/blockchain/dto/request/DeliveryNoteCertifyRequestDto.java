package ar.edu.unlam.tpi.blockchain.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeliveryNoteCertifyRequestDto {


    private byte[] data;

    @JsonCreator
    public DeliveryNoteCertifyRequestDto(@JsonProperty("data") byte[] data) {
        this.data = data;
    }

}
