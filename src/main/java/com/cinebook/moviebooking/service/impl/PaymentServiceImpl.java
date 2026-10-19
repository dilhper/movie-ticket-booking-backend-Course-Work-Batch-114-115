package com.cinebook.moviebooking.service.impl;

import com.cinebook.moviebooking.dto.request.PaymentRequest;
import com.cinebook.moviebooking.dto.response.PaymentResponse;
import com.cinebook.moviebooking.entity.Booking;
import com.cinebook.moviebooking.entity.Payment;
import com.cinebook.moviebooking.entity.enums.BookingStatus;
import com.cinebook.moviebooking.entity.enums.PaymentStatus;
import com.cinebook.moviebooking.exception.BadRequestException;
import com.cinebook.moviebooking.exception.ConflictException;
import com.cinebook.moviebooking.exception.ResourceNotFoundException;
import com.cinebook.moviebooking.exception.UnauthorizedException;
import com.cinebook.moviebooking.repository.BookingRepository;
import com.cinebook.moviebooking.repository.PaymentRepository;
import com.cinebook.moviebooking.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request, String currentUserEmail, boolean isAdmin) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + request.getBookingId()));

        if (!isAdmin && !booking.getUser().getEmail().equalsIgnoreCase(currentUserEmail)) {
            throw new UnauthorizedException("You are not authorized to make payment for this booking");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Cannot make payment for a cancelled booking");
        }

        if (booking.getPayment() != null && booking.getPayment().getStatus() == PaymentStatus.SUCCESS) {
            throw new ConflictException("Payment has already been processed for this booking");
        }

        if (Math.abs(booking.getTotalAmount() - request.getAmount()) > 0.01) {
            throw new BadRequestException("Payment amount (" + request.getAmount() + ") does not match booking total amount (" + booking.getTotalAmount() + ")");
        }

        // Generate unique transaction ID
        String transactionId = "TXN-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Payment payment = booking.getPayment();
        if (payment == null) {
            payment = new Payment();
            payment.setBooking(booking);
        }

        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(transactionId);
        payment.setPaymentDate(LocalDateTime.now());

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPayment(payment);

        Payment savedPayment = paymentRepository.save(payment);
        bookingRepository.save(booking);

        return PaymentResponse.fromEntity(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id, String currentUserEmail, boolean isAdmin) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        if (!isAdmin && !payment.getBooking().getUser().getEmail().equalsIgnoreCase(currentUserEmail)) {
            throw new UnauthorizedException("You are not authorized to view this payment");
        }

        return PaymentResponse.fromEntity(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByBookingId(Long bookingId, String currentUserEmail, boolean isAdmin) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking id: " + bookingId));

        if (!isAdmin && !payment.getBooking().getUser().getEmail().equalsIgnoreCase(currentUserEmail)) {
            throw new UnauthorizedException("You are not authorized to view this payment");
        }

        return PaymentResponse.fromEntity(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(PaymentResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
