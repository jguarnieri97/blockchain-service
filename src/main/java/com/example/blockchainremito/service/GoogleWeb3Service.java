package com.example.blockchainremito.service;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;


public interface GoogleWeb3Service {

    public String publishHashToBlockchain(String fromPrivateKey, String hashHex) throws Exception;

    /**
     * Funcion asyncrona que obtiene el metadata de la transaccion
     * esto es debido a que el bloque puede tardar en ser generado
     * por lo que se debe esperar a que se genere el bloque
     * y luego obtener el metadata de la transaccion
     */
    @Async
    public CompletableFuture<Map<String, Object>> getTxMetadata(String txHash) throws Exception;

    public String retrieveTheHashByHisTransaction(String txHash); 
    
}
