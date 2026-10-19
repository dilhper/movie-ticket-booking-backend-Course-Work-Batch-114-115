package com.cinebook.moviebooking.controller;

import com.cinebook.moviebooking.dto.request.BookingRequest;
import com.cinebook.moviebooking.dto.response.ApiResponse;
import com.cinebook.moviebooking.dto.response.BookingResponse;
import com.cinebook.moviebooking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication
    ) {
        BookingResponse booking = bookingService.createBooking(request, authentication.getName());
        return new ResponseEntity<>(
                ApiResponse.success("Booking created successfully. Please proceed with payment.", booking),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings(Authentication authentication) {
        List<BookingResponse> bookings = bookingService.getUserBookings(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", bookings));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookingsAlias(Authentication authentication) {
        List<BookingResponse> bookings = bookingService.getUserBookings(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", bookings));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings() {
        List<BookingResponse> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(ApiResponse.success("All bookings retrieved successfully", bookings));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        BookingResponse booking = bookingService.getBookingById(id, authentication.getName(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success("Booking retrieved successfully", booking));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
            @PathVariable Long id,
            Authentication authentication
    ) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        BookingResponse booking = bookingService.cancelBooking(id, authentication.getName(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully", booking));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBookingPost(
            @PathVariable Long id,
            Authentication authentication
    ) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        BookingResponse booking = bookingService.cancelBooking(id, authentication.getName(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully", booking));
    }
}
