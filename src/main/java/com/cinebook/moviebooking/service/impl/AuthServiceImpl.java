package com.cinebook.moviebooking.service.impl;

import com.cinebook.moviebooking.dto.request.SigninRequest;
import com.cinebook.moviebooking.dto.request.SignupRequest;
import com.cinebook.moviebooking.dto.response.AuthResponse;
import com.cinebook.moviebooking.dto.response.UserResponse;
import com.cinebook.moviebooking.entity.User;
import com.cinebook.moviebooking.entity.enums.Role;
import com.cinebook.moviebooking.exception.ConflictException;
import com.cinebook.moviebooking.repository.UserRepository;
import com.cinebook.moviebooking.security.CustomUserDetails;
import com.cinebook.moviebooking.security.JwtUtils;
import com.cinebook.moviebooking.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        logger.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            logger.warn("Signup failed: Email already exists - {}", request.getEmail());
            throw new ConflictException("Email is already registered: " + request.getEmail());
        }

        Role userRole = request.getRole() != null ? request.getRole() : Role.CUSTOMER;

        User user = new User(
                request.getName(),
                request.getEmail().toLowerCase().trim(),
                passwordEncoder.encode(request.getPassword()),
                request.getPhone(),
                userRole
        );

        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with ID: {} and Role: {}", savedUser.getId(), savedUser.getRole());

        CustomUserDetails userDetails = CustomUserDetails.build(savedUser);
        String jwt = jwtUtils.generateToken(userDetails);

        return new AuthResponse(jwt, UserResponse.fromEntity(savedUser));
    }

    @Override
    public AuthResponse signin(SigninRequest request) {
        logger.info("Authenticating user with email: {}", request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase().trim(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ConflictException("User record not found"));

        logger.info("User authenticated successfully: {} (Role: {})", user.getEmail(), user.getRole());
        return new AuthResponse(jwt, UserResponse.fromEntity(user));
    }
}
