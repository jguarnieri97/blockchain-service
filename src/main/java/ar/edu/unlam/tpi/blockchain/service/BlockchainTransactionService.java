package ar.edu.unlam.tpi.blockchain.service;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import ar.edu.unlam.tpi.blockchain.dto.response.BlockchainCertResponseDTO;
import java.util.Map;
import java.util.Optional;

public interface BlockchainTransactionService {

    Optional<Transaction> getTransaction(String txHash) throws Exception;
    Optional<Block> getBlockByTransaction(Transaction tx) throws Exception;
    BlockchainCertResponseDTO getBlockChainCertResponse(Map<String, Object> txMetadata, String hash);
    RawTransaction buildTransaction(Credentials credentials, String data) throws Exception;
    String sendTransaction(Credentials credentials, Long chainId, RawTransaction tx) throws Exception;
}
