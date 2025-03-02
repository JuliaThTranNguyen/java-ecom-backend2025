package com.example.fakestore.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Converter {
    public static <T> T toModel(final Object obj, final Class<T> zClass) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
        return modelMapper.map(obj, zClass);
    }

    public static <T, Y> List<T> toList(final List<Y> list, final Class<T> zClass) {
        return list.stream().map(e -> toModel(e, zClass)).toList();
    }
}
