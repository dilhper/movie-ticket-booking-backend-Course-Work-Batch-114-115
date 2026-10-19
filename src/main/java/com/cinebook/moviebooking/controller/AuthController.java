package com.cinebook.moviebooking.controller;

import com.cinebook.moviebooking.dto.request.SigninRequest;
import com.cinebook.moviebooking.dto.request.SignupRequest;
import com.cinebook.moviebooking.dto.response.ApiResponse;
import com.cinebook.moviebooking.dto.response.AuthResponse;
import com.cinebook.moviebooking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping({"/signup", "/register"})
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody SignupRequest request) {
        AuthResponse response = authService.signup(request);
        return new ResponseEntity<>(
                ApiResponse.success("User registered successfully", response),
                HttpStatus.CREATED
        );
    }

    @PostMapping({"/signin", "/login"})
    public ResponseEntity<ApiResponse<AuthResponse>> authenticate(@Valid @RequestBody SigninRequest request) {
        AuthResponse response = authService.signin(request);
        return ResponseEntity.ok(ApiResponse.success("Authentication successful", response));
    }
}
