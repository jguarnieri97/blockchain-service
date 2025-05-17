package com.example.blockchainremito.service;

public interface BlockchainService {
    public String calcularHash(byte[] data);
    public boolean verificarIntegridad(Long id);
}
