package ar.edu.unlam.tpi.blockchain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import ar.edu.unlam.tpi.blockchain.dto.response.BlockchainCertResponseDTO;
import ar.edu.unlam.tpi.blockchain.service.BlockchainTransactionService;
import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlockchainTransactionServiceImpl implements BlockchainTransactionService {

    private final Web3j web3j;

    @Override
    public Optional<Transaction> getTransaction(String txHash) throws Exception {
        Optional<Transaction> tx = Optional.empty();
        while (tx.isEmpty() || tx.get().getBlockHash() == null || tx.get().getBlockHash().isEmpty()) {
            tx = web3j.ethGetTransactionByHash(txHash).sendAsync().get().getTransaction();
            if (tx.isEmpty() || tx.get().getBlockHash() == null) {
                Thread.sleep(1000);
            }
        }
        return tx;
    }

    @Override
    public Optional<EthBlock.Block> getBlockByTransaction(Transaction tx) throws Exception {
        Optional<EthBlock.Block> block = Optional.empty();
        while (block.isEmpty()) {
            block = Optional.ofNullable(web3j.ethGetBlockByHash(tx.getBlockHash(), false).sendAsync().get().getBlock());
            if (block.isEmpty()) {
                Thread.sleep(1000);
            }
        }
        return block;
    }

    @Override
    public BlockchainCertResponseDTO getBlockChainCertResponse(Map<String, Object> txMetadata, String hash) {
        Integer blockNumber = ((BigInteger) txMetadata.get("blockNumber")).intValue();
        return BlockchainCertResponseDTO.builder()
                .txHash((String) txMetadata.get("txHash"))
                .dataHash(hash)
                .blockNumber(blockNumber)
                .build();
    }

    

    @Override
    public RawTransaction buildTransaction(Credentials credentials, String data) throws Exception {
        Map<String, BigInteger> credentialsProperties = generateBlockhainTransactionProperties(credentials);
        return RawTransaction.createTransaction(credentialsProperties.get("nonce"),
            credentialsProperties.get("gasprice"), 
            credentialsProperties.get("gaslimit"), 
            credentials.getAddress(), 
            BigInteger.ZERO,
            data);
    }


    @Override
    public String sendTransaction(Credentials credentials, Long chainId, RawTransaction tx) throws Exception {
        byte[] signedMessage = TransactionEncoder.signMessage(tx, chainId, credentials);
        EthSendTransaction sendTx = web3j.ethSendRawTransaction(Numeric.toHexString(signedMessage)).send();
        if (sendTx.hasError()) 
            throw new RuntimeException("Error al enviar tx: " + sendTx.getError().getMessage());
        
        return sendTx.getTransactionHash();
    }


    private Map<String, BigInteger> generateBlockhainTransactionProperties(Credentials credential) throws Exception {
        return (Map<String, BigInteger>) Map.of(
            "nonce", getNonceBlockChainTransaction(credential),
            "gaslimit", getGasLimitBlockChainTransaction(),
            "gasprice", getGasPriceBlockChainTransaction()
        );
        
    }

    private BigInteger getNonceBlockChainTransaction(Credentials credentials) throws Exception {
        return web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                       .send().getTransactionCount();
    }

    private BigInteger getGasPriceBlockChainTransaction() {
        return Convert.toWei("20", Convert.Unit.GWEI).toBigInteger();
    }

    private BigInteger getGasLimitBlockChainTransaction() {
        return BigInteger.valueOf(50000);
    }
    
}
