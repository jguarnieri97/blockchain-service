package ar.edu.unlam.tpi.blockchain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import ar.edu.unlam.tpi.blockchain.service.TransactionBuilderService;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class TransactionBuilderServiceImpl implements TransactionBuilderService {

    private final Web3j web3j;

    @Override
    public String buildAndSendTransaction(String fromPrivateKey, String data, Long chainId) throws Exception {
        Credentials credentials = Credentials.create(fromPrivateKey);
        BigInteger nonce = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                .send().getTransactionCount();
        BigInteger gasPrice = Convert.toWei("20", Convert.Unit.GWEI).toBigInteger();
        BigInteger gasLimit = BigInteger.valueOf(50000);

        RawTransaction tx = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, credentials.getAddress(), BigInteger.ZERO, data);
        byte[] signedMessage = TransactionEncoder.signMessage(tx, chainId, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction sendTx = web3j.ethSendRawTransaction(hexValue).send();
        if (sendTx.hasError()) {
            throw new RuntimeException("Error al enviar tx: " + sendTx.getError().getMessage());
        }
        return sendTx.getTransactionHash();
    }

}
