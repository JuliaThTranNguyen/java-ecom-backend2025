package com.example.fakestore.dto.response;

import com.example.fakestore.entity.PaymentMethod;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    Long paymentId;
    OrderResponse order;
    LocalDateTime paymentDate;
    PaymentMethod paymentMethod;
    Boolean status;
}
