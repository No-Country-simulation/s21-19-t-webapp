package com.backend.urbia.util;

public class ModelMapper {

    private static final org.modelmapper.ModelMapper mapper = new org.modelmapper.ModelMapper();

    public static <D, T> D map(T entity, Class<D> outClass) {
        return mapper.map(entity, outClass);
    }
}
