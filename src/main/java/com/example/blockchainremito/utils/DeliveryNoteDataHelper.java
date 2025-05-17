package com.example.blockchainremito.utils;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.core.io.ClassPathResource;

import com.example.blockchainremito.dto.DeliveryNoteDto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DeliveryNoteDataHelper {
 
    public static DeliveryNoteDto getDeliveryNoteDto() {
        try {
            ClassPathResource resource = new ClassPathResource("Remito.pdf");
            byte[] data = resource.getInputStream().readAllBytes();
            
            return DeliveryNoteDto.builder()
                    .id(1L)
                    .createdAt(LocalDate.now())
                    .data(data)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load PDF resource", e);
        }
    }
}
