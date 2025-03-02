package com.example.fakestore.service;

import com.example.fakestore.dto.request.UserRequest;
import com.example.fakestore.dto.response.PageResponse;
import com.example.fakestore.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    PageResponse<UserResponse> getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir);

    UserResponse getUser(Long userId);

    UserResponse getProfile();

    UserResponse updateProfile(UserRequest userRequest);

    void deleteUser(Long userId);
}
