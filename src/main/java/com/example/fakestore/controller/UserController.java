package com.example.fakestore.controller;

import com.example.fakestore.dto.request.UserRequest;
import com.example.fakestore.dto.response.PageResponse;
import com.example.fakestore.dto.response.UserResponse;
import com.example.fakestore.service.UserService;
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
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(name = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION) String sortDir
    ){
        var response = userService.getAllUsers(pageNo, pageSize, sortBy, sortDir);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId){
        var response = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(){
        var response = userService.getProfile();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody UserRequest userRequest){
        var response = userService.updateProfile(userRequest);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body("user deleted");
    }


}
