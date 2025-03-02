package com.example.fakestore.service.impl;

import com.example.fakestore.converter.Converter;
import com.example.fakestore.dto.request.CategoryRequest;
import com.example.fakestore.dto.response.CategoryResponse;
import com.example.fakestore.dto.response.PageResponse;
import com.example.fakestore.entity.Category;
import com.example.fakestore.exception.ApiException;
import com.example.fakestore.exception.ErrorCode;
import com.example.fakestore.repository.CategoryRepository;
import com.example.fakestore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public CategoryResponse addCategory(CategoryRequest categoryRequest) {
        var existingCategory = categoryRepository.findByName(categoryRequest.getName());
        if (existingCategory.isPresent())
            throw new ApiException(ErrorCode.BAD_REQUEST, "category already exists");

        Category category = Category.builder()
                .name(categoryRequest.getName())
                .image(categoryRequest.getImage())
                .build();

        Category newCategory = categoryRepository.save(category);

        return Converter.toModel(newCategory, CategoryResponse.class);
    }

    @Override
    public CategoryResponse getCategory(Long categoryId) {
        Category existingCategory = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "category not found"));

        return Converter.toModel(existingCategory, CategoryResponse.class);
    }

    @Override
    public PageResponse<CategoryResponse> getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Page<Category> categoryData = categoryRepository.findAll(pageable);

        List<Category> categoryList = categoryData.getContent();
        List<CategoryResponse> content = Converter.toList(categoryList, CategoryResponse.class);

        return PageResponse.<CategoryResponse>builder()
                .content(content)
                .pageNo(categoryData.getNumber())
                .pageSize(categoryData.getSize())
                .totalElements(categoryData.getTotalElements())
                .totalPages(categoryData.getTotalPages())
                .last(categoryData.isLast())
                .build();
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category existingCategory = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "category not found"));

        categoryRepository.delete(existingCategory);

        return "category deleted";
    }
}
