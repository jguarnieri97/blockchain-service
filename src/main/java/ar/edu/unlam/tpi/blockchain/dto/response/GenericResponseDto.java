package ar.edu.unlam.tpi.blockchain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponseDto<T> {

    private int code;
    private String message;
    private T data;

}
