package ar.edu.unlam.tpi.blockchain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;

import ar.edu.unlam.tpi.blockchain.service.BlockchainTransactionService;

import java.util.Optional;

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

}
