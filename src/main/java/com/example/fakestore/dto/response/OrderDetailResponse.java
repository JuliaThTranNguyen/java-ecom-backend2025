package com.example.fakestore.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailResponse {
    Long orderDetailId;
    OrderResponse order;
    ProductResponse product;
    int quantity;
    double subtotal;
}
