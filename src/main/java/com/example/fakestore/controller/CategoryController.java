package com.example.fakestore.controller;

import com.example.fakestore.dto.request.CategoryRequest;
import com.example.fakestore.dto.response.CategoryResponse;
import com.example.fakestore.dto.response.PageResponse;
import com.example.fakestore.service.CategoryService;
import com.example.fakestore.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular frontend
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> addCategory(@RequestBody CategoryRequest categoryRequest) {
        CategoryResponse response = categoryService.addCategory(categoryRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long categoryId) {
        CategoryResponse response = categoryService.getCategory(categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/categories")
    public ResponseEntity<PageResponse<CategoryResponse>> getAllCategories(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        PageResponse<CategoryResponse> response = categoryService.getAllCategories(pageNo, pageSize, sortBy, sortDir);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        String statement = categoryService.deleteCategory(categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(statement);
    }
}
