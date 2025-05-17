package com.example.blockchainremito.exceptions;

import com.example.blockchainremito.dto.ErrorResponse;

public class DeliveryNoteClientException extends GenericException{

    public DeliveryNoteClientException(ErrorResponse error) {
        super(error.getCode(), error.getMessage(), error.getDetail());
    }

}
