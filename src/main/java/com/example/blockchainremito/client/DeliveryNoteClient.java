package com.example.blockchainremito.client;

import com.example.blockchainremito.dto.DeliveryNoteDto;

/**
 * Cliente para conectar con el servicio Contracts API
 */
public interface DeliveryNoteClient {

    /**
     * Recurso para obtener remito por ID
     * desde el servicio Contracts API
     *
     * @param id id del remito
     * @return el remito detallado
     */
    DeliveryNoteDto getDeliveryNoteById(Long id);

}