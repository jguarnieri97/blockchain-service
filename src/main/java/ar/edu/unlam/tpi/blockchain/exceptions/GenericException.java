package ar.edu.unlam.tpi.blockchain.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class GenericException extends RuntimeException {

    private int code;
    private String message;
    private String detail;

    public GenericException(int code, String message, String detail) {
        super(message);
        this.code = code;
        this.message = message;
        this.detail = detail;
    }
}

