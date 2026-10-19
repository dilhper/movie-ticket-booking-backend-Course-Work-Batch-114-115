package com.cinebook.moviebooking.service;

import com.cinebook.moviebooking.dto.request.SigninRequest;
import com.cinebook.moviebooking.dto.request.SignupRequest;
import com.cinebook.moviebooking.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse signup(SignupRequest request);
    AuthResponse signin(SigninRequest request);
}
