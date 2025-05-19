package ar.edu.unlam.tpi.blockchain.exceptions;

import org.springframework.http.HttpStatus;

public class HashInvalidException extends GenericException {

    private static final String MESSAGE = "CONFLICT";
    public HashInvalidException(String details) {
        super(HttpStatus.CONFLICT.value(),MESSAGE,details);
    }

}