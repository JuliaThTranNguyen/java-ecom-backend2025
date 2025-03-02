package com.example.fakestore.service.impl;

import com.example.fakestore.converter.Converter;
import com.example.fakestore.dto.request.UserRequest;
import com.example.fakestore.dto.response.PageResponse;
import com.example.fakestore.dto.response.UserResponse;
import com.example.fakestore.entity.User;
import com.example.fakestore.exception.ApiException;
import com.example.fakestore.exception.ErrorCode;
import com.example.fakestore.repository.UserRepository;
import com.example.fakestore.security.service.UserDetailsImpl;
import com.example.fakestore.service.UserService;
import com.example.fakestore.util.JwtSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public PageResponse<UserResponse> getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Page<User> userData = userRepository.findAll(pageable);

        List<User> userList = userData.getContent();
        List<UserResponse> content = Converter.toList(userList, UserResponse.class);

        return PageResponse.<UserResponse>builder()
                .content(content)
                .pageNo(userData.getNumber())
                .pageSize(userData.getSize())
                .totalElements(userData.getTotalElements())
                .totalPages(userData.getTotalPages())
                .last(userData.isLast())
                .build();
    }

    @Override
    public UserResponse getUser(Long userId) {
        User existingUser = userRepository.findByUserId(userId).orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "user not found"));

        return Converter.toModel(existingUser, UserResponse.class);
    }

    @Override
    public UserResponse getProfile() {
        UserDetailsImpl userDetails = JwtSecurityUtil.getJwtUserInfo().orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "user not found"));

        var profile = userRepository.findByEmail(userDetails.getEmail());

        return Converter.toModel(profile, UserResponse.class);
    }

    @Override
    public UserResponse updateProfile(UserRequest userRequest) {
        UserDetailsImpl userDetails = JwtSecurityUtil.getJwtUserInfo().orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "user not found"));

        var profile = userRepository.findByEmail(userDetails.getEmail());

        profile.get().setFirstName(userRequest.getFirstName());
        profile.get().setLastName(userRequest.getLastName());
        profile.get().setEmail(userRequest.getEmail());
        profile.get().setAvatar(userRequest.getAvatar());

        User updatedProfile = userRepository.save(profile.get());

        return Converter.toModel(updatedProfile, UserResponse.class);
    }


    @Override
    public void deleteUser(Long userId) {
        User existingUser = userRepository.findByUserId(userId).orElseThrow(() ->
                new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "user not found"));

        userRepository.delete(existingUser);
    }
}
