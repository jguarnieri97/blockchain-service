package com.example.blockchainremito.service.impl;

import com.example.blockchainremito.client.DeliveryNoteClient;
import com.example.blockchainremito.dto.DeliveryNoteDto;
import com.example.blockchainremito.service.DeliveryNoteService;
import com.example.blockchainremito.utils.DeliveryNoteDataHelper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DeliveryNoteServiceImpl implements DeliveryNoteService {
    DeliveryNoteClient deliveryNoteClient;

    @Override
    public DeliveryNoteDto getDeliveryNoteById(Long id) {
        //return deliveryNoteClient.getDeliveryNoteById(id);
        return DeliveryNoteDataHelper.getDeliveryNoteDto();
    }

}
