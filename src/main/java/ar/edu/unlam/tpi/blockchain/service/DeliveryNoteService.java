package ar.edu.unlam.tpi.blockchain.service;

import org.springframework.scheduling.annotation.Async;
import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteCertifyRequestDto;
import ar.edu.unlam.tpi.blockchain.dto.request.DeliveryNoteVerifyRequestDto;
import ar.edu.unlam.tpi.blockchain.dto.response.BlockchainCertResponseDTO;
import ar.edu.unlam.tpi.blockchain.dto.response.MessageResponseDto;

import java.util.concurrent.CompletableFuture;

/**
 * Servicio encargado de certificar y verificar remitos (delivery notes) en la blockchain.
 */
public interface DeliveryNoteService {

    /**
     * Certifica un remito enviando la información relevante a la blockchain.
     * Este método se ejecuta de forma asíncrona.
     *
     * @param request Objeto que contiene los datos necesarios para la certificación del remito.
     * @return Un {@link CompletableFuture} que resuelve con un {@link BlockchainCertResponseDTO} 
     *         que contiene la respuesta de la certificación.
     */
    @Async
    CompletableFuture<BlockchainCertResponseDTO> certifyDeliveryNote(DeliveryNoteCertifyRequestDto request);

    /**
     * Verifica la validez de un remito previamente certificado en la blockchain.
     *
     * @param request Objeto que contiene los datos necesarios para realizar la verificación.
     * @return Un {@link MessageResponseDto} indicando el resultado de la verificación (éxito o error).
     */
    MessageResponseDto verifyDeliveryNote(DeliveryNoteVerifyRequestDto request);

}
