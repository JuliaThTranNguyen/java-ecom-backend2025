package com.example.fakestore.service;

import com.example.fakestore.dto.request.CategoryRequest;
import com.example.fakestore.dto.response.CategoryResponse;
import com.example.fakestore.dto.response.PageResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse addCategory(CategoryRequest categoryRequest);

    CategoryResponse getCategory(Long categoryId);

    PageResponse<CategoryResponse> getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir);

    String deleteCategory(Long categoryId);
}
