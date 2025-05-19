package ar.edu.unlam.tpi.blockchain.service;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public interface GoogleWeb3Service {

    /**
     * Publica un hash en la blockchain.
     *
     * @param fromPrivateKey Clave privada del remitente.
     * @param hashHex Hash en formato hexadecimal a publicar.
     * @return Hash de la transacción en la blockchain.
     * @throws Exception Si ocurre un error durante la publicación.
     */
    String publishHashToBlockchain(String fromPrivateKey, String hashHex) throws Exception;

    /**
     * Obtiene los metadatos de una transacción en la blockchain.
     *
     * @param txHash Hash de la transacción.
     * @return Metadatos de la transacción como un mapa.
     * @throws Exception Si ocurre un error al obtener los metadatos.
     */
    CompletableFuture<Map<String, Object>> getTxMetadata(String txHash) throws Exception;

    String getHashFromTransaction(String txHash);
    
}
