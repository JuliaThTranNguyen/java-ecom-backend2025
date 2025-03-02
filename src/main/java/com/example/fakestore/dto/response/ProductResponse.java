package com.example.fakestore.dto.response;

import com.example.fakestore.entity.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductResponse {
    Long productId;
    String title;
    double price;
    String description;
    CategoryResponse category;
    String image;
    int stock;

}
