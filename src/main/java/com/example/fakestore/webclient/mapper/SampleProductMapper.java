package com.example.fakestore.webclient.mapper;

import com.example.fakestore.entity.Product;
import com.example.fakestore.webclient.dto.SampleProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SampleProductMapper {

    Product toProduct(SampleProduct product);
}
