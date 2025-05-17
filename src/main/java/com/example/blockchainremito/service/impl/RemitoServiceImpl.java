package com.example.blockchainremito.service.impl;

import com.example.blockchainremito.model.Remito;
import com.example.blockchainremito.repository.RemitoRepository;
import com.example.blockchainremito.service.BlockchainService;
import com.example.blockchainremito.service.GoogleWeb3Service;
import com.example.blockchainremito.service.RemitoService;
import com.example.blockchainremito.utils.DeliveryNoteDataHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RemitoServiceImpl implements RemitoService {

    RemitoRepository remitoRepository;
    BlockchainService blockchainService;
    GoogleWeb3Service googleWeb3Service;
    
    @Qualifier("privateKey")
    String privateKey;

    @Async
    @Override
    public CompletableFuture<Remito> guardarRemito(Remito remito) throws Exception {
        remito.setData(DeliveryNoteDataHelper.getDeliveryNoteDto().getData());

       // String datosAFirmar = generarDatosParaHash(remito);
        byte[] datosAFirmar = remito.getData();
        String hash = blockchainService.calcularHash(datosAFirmar);
        remito.setHash(hash);

        
        //Publicar el hash en la blockchain
        String txHash = googleWeb3Service.publishHashToBlockchain(privateKey, hash);
        remito.setTxHash(txHash); //Establezo el txHash
            
        // Ahora todo el resto en forma asíncrona
        return googleWeb3Service.getTxMetadata(txHash)
        .thenCompose(txMetadata -> {
            remito.setBlockNumber(((BigInteger) txMetadata.get("blockNumber")).intValue());
            remito.setTimestamp((Instant) txMetadata.get("timestamp"));


            log.info("REMITO GUARDADO EN LA BLOCKCHAIN!!!!! : " + remito.getTxHash());
            return CompletableFuture.supplyAsync(() -> remitoRepository.save(remito));
        });
    }   

    @Override
    public List<Remito> obtenerTodos() {
        return remitoRepository.findAll();
    }

}
