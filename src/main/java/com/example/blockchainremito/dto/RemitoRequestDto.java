package com.example.blockchainremito.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)   
public class RemitoRequestDto {
    
    private LocalDate fecha;

    private String empresaSolicitante;

    private String empresaProveedora;

    private String descripcionTrabajo;

    private String tecnicoAsignado;

    private String duracionEstimada;


}
