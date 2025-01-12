package com.example.fakestore.service.impl;

import com.example.fakestore.converter.Converter;
import com.example.fakestore.dto.request.ProductRequest;
import com.example.fakestore.dto.response.PageResponse;
import com.example.fakestore.dto.response.ProductResponse;
import com.example.fakestore.entity.Category;
import com.example.fakestore.entity.Product;
import com.example.fakestore.exception.ApiException;
import com.example.fakestore.exception.ErrorCode;
import com.example.fakestore.repository.CategoryRepository;
import com.example.fakestore.repository.ProductRepository;
import com.example.fakestore.security.service.UserDetailsImpl;
import com.example.fakestore.service.ProductService;
import com.example.fakestore.util.JwtSecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponse addProduct(ProductRequest productRequest) {
        Category addedCategory = categoryRepository.findByName(productRequest.getCategory().getName())
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "category not found"));

        Product product = Product.builder()
                .title(productRequest.getTitle())
                .description(productRequest.getDescription())
                .category(addedCategory)
                .image(productRequest.getImage())
                .stock(productRequest.getStock())
                .build();

        Product newProduct = productRepository.save(product);

        return Converter.toModel(newProduct, ProductResponse.class);
    }

    @Override
    public ProductResponse getProduct(Long productId) {
        Product existingProduct = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "product not found"));

        return Converter.toModel(existingProduct, ProductResponse.class);
    }

    @Override
    public PageResponse<ProductResponse> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> productData = productRepository.findAll(pageable);

        List<Product> productList = productData.getContent();
        List<ProductResponse> content = Converter.toList(productList, ProductResponse.class);

        return PageResponse.<ProductResponse>builder()
                .content(content)
                .pageNo(productData.getNumber())
                .pageSize(productData.getSize())
                .totalElements(productData.getTotalElements())
                .totalPages(productData.getTotalPages())
                .last(productData.isLast())
                .build();
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductRequest productRequest) {
        Product existingProduct = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "product not found"));

        Category updatedCategory = categoryRepository.findByName(productRequest.getCategory().getName())
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "category not found"));

        existingProduct.setTitle(productRequest.getTitle());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setStock(productRequest.getStock());
        existingProduct.setImage(productRequest.getImage());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setCategory(updatedCategory);

        Product updatedProduct = productRepository.save(existingProduct);

        return Converter.toModel(updatedProduct, ProductResponse.class);
    }

    @Override
    public String deleteProduct(Long productId) {
        Product existingProduct = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "product not found"));

        productRepository.delete(existingProduct);

        return "product deleted";
    }
}
