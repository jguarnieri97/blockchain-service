package com.example.blockchainremito.controller.impl;

import org.springframework.web.bind.annotation.RestController;

import com.example.blockchainremito.dto.RemitoRequestDto;
import com.example.blockchainremito.model.Remito;
import com.example.blockchainremito.service.RemitoService;
import com.example.blockchainremito.utils.Converter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/remitos")
public class SaveController {
       
    private final RemitoService remitoService;

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
        */
        return remitoService.guardarRemito(remito)
                .thenApply(ResponseEntity::ok);
    }

}
