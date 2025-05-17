package com.example.blockchainremito.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import com.example.blockchainremito.dto.GenericResponse;
import com.example.blockchainremito.dto.RemitoRequestDto;
import com.example.blockchainremito.model.Remito;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequestMapping("/remitos")
public interface RemitoController {
 
    @GetMapping("/{id}/verificar")
    @ResponseStatus(HttpStatus.OK)
    GenericResponse<String> verificarHash(@PathVariable Long id);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    GenericResponse<List<Remito>> listarRemitos();
}
