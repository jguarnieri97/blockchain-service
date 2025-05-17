package com.example.blockchainremito.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DeliveryNoteDto {

    private Long id;
    private byte[] data;
    private LocalDate createdAt;

}
