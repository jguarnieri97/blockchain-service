package com.example.blockchainremito.service;

import com.example.blockchainremito.model.Remito;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface RemitoService {
    
    @Async
    public CompletableFuture<Remito> guardarRemito(Remito remito) throws Exception;

    public List<Remito> obtenerTodos();
}
