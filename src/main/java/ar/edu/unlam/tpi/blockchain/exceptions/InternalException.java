package ar.edu.unlam.tpi.blockchain.exceptions;

import org.springframework.http.HttpStatus;

public class InternalException extends GenericException {
    
    private static final String MESSAGE = "INTERNAL_SERVER_ERROR";
    public InternalException(String detail) {
        super(HttpStatus.INTERNAL_SERVER_ERROR.value(), MESSAGE, detail);
    }
}
