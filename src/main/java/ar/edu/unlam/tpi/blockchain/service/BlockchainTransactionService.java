package ar.edu.unlam.tpi.blockchain.service;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import ar.edu.unlam.tpi.blockchain.dto.response.BlockchainCertResponseDTO;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio que maneja operaciones relacionadas con transacciones en la blockchain.
 */
public interface BlockchainTransactionService {

    /**
     * Obtiene una transacción de la blockchain a partir de su hash.
     *
     * @param txHash El hash de la transacción.
     * @return Un {@link Optional} que contiene la transacción si se encuentra.
     * @throws Exception Si ocurre un error al consultar la blockchain.
     */
    Optional<Transaction> getTransaction(String txHash) throws Exception;

    /**
     * Obtiene el bloque que contiene una transacción específica.
     *
     * @param tx La transacción cuya información de bloque se desea obtener.
     * @return Un {@link Optional} que contiene el bloque si se encuentra.
     * @throws Exception Si ocurre un error durante la consulta.
     */
    Optional<Block> getBlockByTransaction(Transaction tx) throws Exception;

    /**
     * Genera una respuesta personalizada con los metadatos de una transacción y su hash.
     *
     * @param txMetadata Metadatos relacionados con la transacción (pueden incluir datos del contexto de negocio).
     * @param hash El hash de la transacción.
     * @return Un objeto {@link BlockchainCertResponseDTO} que encapsula la respuesta generada.
     */
    BlockchainCertResponseDTO getBlockChainCertResponse(Map<String, Object> txMetadata, String hash);

    /**
     * Construye una transacción sin firmar con los datos provistos.
     *
     * @param credentials Credenciales que incluyen la dirección de origen.
     * @param data Los datos a incluir en la transacción (ej. payload de contrato).
     * @return La transacción construida.
     * @throws Exception Si ocurre un error durante la construcción.
     */
    RawTransaction buildTransaction(Credentials credentials, String data) throws Exception;

    /**
     * Firma y envía una transacción a la blockchain.
     *
     * @param credentials Credenciales necesarias para firmar la transacción.
     * @param chainId El identificador de la red blockchain (chain ID).
     * @param tx La transacción a enviar.
     * @return El hash de la transacción enviada.
     * @throws Exception Si ocurre un error al firmar o enviar la transacción.
     */
    String sendTransaction(Credentials credentials, Long chainId, RawTransaction tx) throws Exception;
}
