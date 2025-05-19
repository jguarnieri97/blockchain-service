package ar.edu.unlam.tpi.blockchain.controller.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;

import ar.edu.unlam.tpi.blockchain.exceptions.HashInvalidException;
import ar.edu.unlam.tpi.blockchain.exceptions.InternalException;
import ar.edu.unlam.tpi.blockchain.dto.response.ErrorResponseDto;


@ControllerAdvice
public class DeliveryNoteControllerException {
 
    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ErrorResponseDto> handleEmptyException(InternalException ex) {
        return ResponseEntity
        .status(ex.getCode())
        .body(ErrorResponseDto.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .detail(ex.getDetail())
                .build());
    }

    @ExceptionHandler(HashInvalidException.class)
    public ResponseEntity<ErrorResponseDto> handleHashException(HashInvalidException ex) {
        return ResponseEntity
        .status(ex.getCode())
        .body(ErrorResponseDto.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .detail(ex.getDetail())
                .build());
    }
}
