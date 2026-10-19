package com.cinebook.moviebooking.service;

import com.cinebook.moviebooking.dto.request.UserUpdateRequest;
import com.cinebook.moviebooking.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UserUpdateRequest request, String currentUserEmail);
    void deleteUser(Long id);
    UserResponse getCurrentUserProfile(String currentUserEmail);
}
