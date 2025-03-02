package com.example.fakestore.dto.request;

import com.example.fakestore.dto.response.CategoryResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    String title;
    double price;
    String description;
    CategoryRequest category;
    String image;
    int stock;
}
