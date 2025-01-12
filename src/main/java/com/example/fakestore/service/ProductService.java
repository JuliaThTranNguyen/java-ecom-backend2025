package com.example.fakestore.service;

import com.example.fakestore.dto.request.ProductRequest;
import com.example.fakestore.dto.response.PageResponse;
import com.example.fakestore.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse addProduct(ProductRequest productRequest);

    ProductResponse getProduct(Long productId);

    PageResponse<ProductResponse> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir);

    ProductResponse updateProduct(Long productId, ProductRequest productRequest);

    String deleteProduct(Long productId);
}
