package com.cinebook.moviebooking.service;

import com.cinebook.moviebooking.dto.request.BookingRequest;
import com.cinebook.moviebooking.dto.response.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request, String userEmail);

    BookingResponse getBookingById(Long id, String currentUserEmail, boolean isAdmin);

    List<BookingResponse> getUserBookings(String userEmail);

    List<BookingResponse> getAllBookings();

    BookingResponse cancelBooking(Long id, String currentUserEmail, boolean isAdmin);
}
