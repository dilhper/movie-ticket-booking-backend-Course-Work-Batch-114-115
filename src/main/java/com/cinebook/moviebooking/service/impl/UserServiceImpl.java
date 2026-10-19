package com.cinebook.moviebooking.service.impl;

import com.cinebook.moviebooking.dto.request.UserUpdateRequest;
import com.cinebook.moviebooking.dto.response.UserResponse;
import com.cinebook.moviebooking.entity.User;
import com.cinebook.moviebooking.entity.enums.Role;
import com.cinebook.moviebooking.exception.ResourceNotFoundException;
import com.cinebook.moviebooking.exception.UnauthorizedException;
import com.cinebook.moviebooking.repository.UserRepository;
import com.cinebook.moviebooking.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        logger.info("Fetching all registered users");
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        logger.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return UserResponse.fromEntity(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request, String currentUserEmail) {
        logger.info("Updating user ID: {} requested by: {}", id, currentUserEmail);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        // Only ADMIN or the user themselves can update their profile
        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to update this profile");
        }

        user.setName(request.getName());
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        User updatedUser = userRepository.save(user);
        logger.info("User ID: {} updated successfully", updatedUser.getId());
        return UserResponse.fromEntity(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        userRepository.delete(user);
        logger.info("User ID: {} deleted successfully", id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUserProfile(String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + currentUserEmail));
        return UserResponse.fromEntity(user);
    }
}
