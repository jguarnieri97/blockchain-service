package com.example.blockchainremito.controller.impl;

import com.example.blockchainremito.controller.RemitoController;
import com.example.blockchainremito.dto.GenericResponse;
import com.example.blockchainremito.dto.RemitoRequestDto;
import com.example.blockchainremito.model.Remito;
import com.example.blockchainremito.service.BlockchainService;
import com.example.blockchainremito.service.RemitoService;
import com.example.blockchainremito.utils.Constants;
import com.example.blockchainremito.utils.Converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RemitoControllerImpl implements RemitoController {

    private final RemitoService remitoService;
    private final BlockchainService blockchainService;
    
    /*
    @Async
    @PostMapping("/crear")
    public CompletableFuture<ResponseEntity<Remito>> crearRemito(@RequestBody RemitoRequestDto remitorRequest) throws Exception {
        Remito remito = Converter.convertToEntity(remitorRequest, Remito.class);
        /*     
        return remitoService.guardarRemito(remito)
                .thenApply(savedRemito -> new GenericResponse<>(
                    Constants.STATUS_CREATED,
                    Constants.SUCCESS_MESSAGE,
                    savedRemito
            ));
        
        return remitoService.guardarRemito(remito)
                .thenApply(ResponseEntity::ok);
    }*/

    @Override
    public GenericResponse<String> verificarHash(Long id) {
        boolean isValid = blockchainService.verificarIntegridad(id);
        return new GenericResponse<>(
            Constants.STATUS_OK,
            Constants.SUCCESS_MESSAGE,
            isValid ? "El remito es válido" : "El remito no es válido"
        );
    }

    @Override
    public GenericResponse<List<Remito>> listarRemitos() {
        List<Remito> remitos = remitoService.obtenerTodos();
        return new GenericResponse<>(
            Constants.STATUS_OK,
            Constants.SUCCESS_MESSAGE,
            remitos
        );
    }
}