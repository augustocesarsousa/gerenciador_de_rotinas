package com.acsousa.gerenciador_de_rotinas.utils.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;

public class ConvertMapper {
    private static final Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    // Converte um objeto origem para um objeto destino
    public static <O, D> D convertObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    // Converte uma lista de objetos origem para uma lista de objetos destino
    public static <O, D> List<D> convertListOfObjects(List<O> origin, Class<D> destination) {
        List<D> destinationObjects = new ArrayList<>();

        for(Object object : origin) {
            destinationObjects.add(mapper.map(object, destination));
        }

        return destinationObjects;
    }
}
