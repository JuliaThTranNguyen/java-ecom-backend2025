package com.example.fakestore.dto.request;

import com.example.fakestore.entity.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String email;
    String firstName;
    String lastName;
    String avatar;
}
