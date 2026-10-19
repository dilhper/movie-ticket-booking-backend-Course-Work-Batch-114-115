package com.cinebook.moviebooking.service;

import com.cinebook.moviebooking.dto.request.PaymentRequest;
import com.cinebook.moviebooking.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse processPayment(PaymentRequest request, String currentUserEmail, boolean isAdmin);

    PaymentResponse getPaymentById(Long id, String currentUserEmail, boolean isAdmin);

    PaymentResponse getPaymentByBookingId(Long bookingId, String currentUserEmail, boolean isAdmin);

    List<PaymentResponse> getAllPayments();
}
