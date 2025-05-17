package com.example.blockchainremito.client.impl;

import com.example.blockchainremito.client.DeliveryNoteClient;
import com.example.blockchainremito.dto.DeliveryNoteDto;
import com.example.blockchainremito.dto.ErrorResponse;
import com.example.blockchainremito.dto.GenericResponse;
import com.example.blockchainremito.exceptions.DeliveryNoteClientException;
import com.example.blockchainremito.utils.annotations.WebHttpClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@Slf4j
@WebHttpClient
@RequiredArgsConstructor
public class DeliveryNoteClientImpl implements DeliveryNoteClient {

    private final WebClient webClient;

    @Value("${contracts.host}")
    private String host;

    @Override
    public DeliveryNoteDto getDeliveryNoteById(Long id) {
        GenericResponse<DeliveryNoteDto> response = webClient.get()
                .uri(host + "contract/" + id)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(ErrorResponse.class)
                                .flatMap(DeliveryNoteClientImpl::handle4xxError))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> clientResponse.bodyToMono(ErrorResponse.class)
                                .flatMap(DeliveryNoteClientImpl::handle5xxError))
                .bodyToMono(new ParameterizedTypeReference<GenericResponse<DeliveryNoteDto>>() {})
                .block();
        assert response != null;
        return response.getData();
    }

    private static Mono<Throwable> handle4xxError(ErrorResponse error) {
        log.error("Error del cliente externo Budgets API: {}", error);
        return Mono.error(new DeliveryNoteClientException(error));
    }

    private static Mono<Throwable> handle5xxError(ErrorResponse error) {
        log.error("Error del servidor externo Budgets API: {}", error);
        return Mono.error(new DeliveryNoteClientException(error));
    }

}