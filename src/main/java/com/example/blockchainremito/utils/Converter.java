package com.example.blockchainremito.utils;

import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class Converter {
    
    private static ModelMapper modelMapper = new ModelMapper();

    public static <T> T convertToEntity(Object object, Class<T> entityClass) {
        return modelMapper.map(object, entityClass);
    }

    public static <T> T convertToDto(Object object, Class<T> dtoClass) {
        return modelMapper.map(object, dtoClass);
    }
}
