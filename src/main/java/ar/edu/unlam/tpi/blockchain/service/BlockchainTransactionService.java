package ar.edu.unlam.tpi.blockchain.service;

import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock.Block;

import java.util.Optional;

public interface BlockchainTransactionService {

    Optional<Transaction> getTransaction(String txHash) throws Exception;
    Optional<Block> getBlockByTransaction(Transaction tx) throws Exception;

}
