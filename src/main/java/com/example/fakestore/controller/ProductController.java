package com.example.fakestore.controller;

import com.example.fakestore.dto.request.ProductRequest;
import com.example.fakestore.dto.response.PageResponse;
import com.example.fakestore.dto.response.ProductResponse;
import com.example.fakestore.service.ProductService;
import com.example.fakestore.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular frontend
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/products")
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse response = productService.addProduct(productRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
        ProductResponse response = productService.getProduct(productId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/products")
    public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        PageResponse<ProductResponse> response = productService.getAllProducts(pageNo, pageSize, sortBy, sortDir);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductRequest productRequest) {

        ProductResponse response = productService.updateProduct(productId, productRequest);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        String statement = productService.deleteProduct(productId);

        return ResponseEntity.status(HttpStatus.OK).body(statement);
    }
}
