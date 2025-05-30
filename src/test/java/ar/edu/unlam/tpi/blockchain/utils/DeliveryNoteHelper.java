package ar.edu.unlam.tpi.blockchain.utils;

import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteCertifyRequestDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DeliveryNoteHelper {
    
    public DeliveryNoteCertifyRequestDto createDeliveryNoteCertifyRequestDto(String data) {
        return DeliveryNoteCertifyRequestDto.builder()
                .data(data.getBytes())
                .build();
    }
}
