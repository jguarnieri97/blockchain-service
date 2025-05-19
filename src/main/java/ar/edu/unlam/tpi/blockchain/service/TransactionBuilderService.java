package ar.edu.unlam.tpi.blockchain.service;

public interface TransactionBuilderService {
    public String buildAndSendTransaction(String fromPrivateKey, String data, Long chainId) throws Exception;
}
