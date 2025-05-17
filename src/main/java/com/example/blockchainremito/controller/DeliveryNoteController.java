package com.example.blockchainremito.controller;

import com.example.blockchainremito.dto.DeliveryNoteDto;
import com.example.blockchainremito.dto.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controlador para las operaciones relacionadas con Contratos
 */
@RequestMapping("/contracts/v1/accounts/")
public interface DeliveryNoteController {

    /**
     * Recurso para obtener una lista de todos los remitos
     * según su id
     *
     * @param deliveryNoteId: id del remito
     * @return deliveryNote
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    GenericResponse<DeliveryNoteDto> getDeliveryNoteById(@RequestParam(required = false) Long deliveryNoteId);

    @GetMapping("/download/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<byte[]> downloadDeliveryNote(@PathVariable Long id);
}
