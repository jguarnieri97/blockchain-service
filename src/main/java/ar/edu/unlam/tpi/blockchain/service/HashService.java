package ar.edu.unlam.tpi.blockchain.service;

import ar.edu.unlam.tpi.blockchain.dto.response.MessageResponseDto;

/**
 * Servicio responsable del cálculo y verificación de hashes, utilizado para garantizar
 * integridad de datos en operaciones críticas como certificación o validación.
 */
public interface HashService {

    /**
     * Calcula el hash de un arreglo de bytes utilizando un algoritmo definido (por ejemplo, SHA-256).
     *
     * @param data Los datos en formato binario a ser hasheados.
     * @return Una cadena de texto que representa el hash hexadecimal calculado.
     */
    String calculateHash(byte[] data);

    /**
     * Compara dos hashes y devuelve una respuesta indicando si coinciden.
     *
     * @param currentHash El hash actual calculado o almacenado.
     * @param hashToCompare El hash con el que se desea comparar.
     * @return Un {@link MessageResponseDto} indicando si los hashes coinciden o no.
     */
    MessageResponseDto checkHash(String currentHash, String hashToCompare);

}
