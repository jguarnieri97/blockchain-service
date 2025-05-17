package com.example.blockchainremito.service.impl;
import jakarta.xml.bind.DatatypeConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.blockchainremito.model.Remito;
import com.example.blockchainremito.repository.RemitoRepository;
import com.example.blockchainremito.service.BlockchainService;
import com.example.blockchainremito.service.GoogleWeb3Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BlockchainServiceImpl implements BlockchainService{

    RemitoRepository remitoRepository;
    GoogleWeb3Service googleWeb3Service;

    @Override
    public String calcularHash(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            return DatatypeConverter.printHexBinary(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No se pudo generar el hash", e);
        }
    }
    
    @Override
    public boolean verificarIntegridad(Long id) {
        Optional<Remito> remitoOpt = remitoRepository.findById(id);
        if (remitoOpt.isEmpty()) return false;

        Remito remito = remitoOpt.get();
        String hashCalculado = calcularHash(remito.getData());


        String hashRetrievedByBlockchain = googleWeb3Service.retrieveTheHashByHisTransaction(remitoOpt.get().getTxHash()); 

        return hashCalculado.equals(hashRetrievedByBlockchain);
    }

}
