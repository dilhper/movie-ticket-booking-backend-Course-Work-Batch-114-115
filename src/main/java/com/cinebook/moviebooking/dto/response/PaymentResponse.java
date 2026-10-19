package com.cinebook.moviebooking.dto.response;

import com.cinebook.moviebooking.entity.Payment;
import com.cinebook.moviebooking.entity.enums.PaymentMethod;
import com.cinebook.moviebooking.entity.enums.PaymentStatus;
import java.time.LocalDateTime;

public class PaymentResponse {

    private Long id;
    private Long bookingId;
    private Double amount;
    private LocalDateTime paymentDate;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String transactionId;

    public PaymentResponse() {
    }

    public static PaymentResponse fromEntity(Payment payment) {
        if (payment == null) return null;
        PaymentResponse dto = new PaymentResponse();
        dto.setId(payment.getId());
        if (payment.getBooking() != null) {
            dto.setBookingId(payment.getBooking().getId());
        }
        dto.setAmount(payment.getAmount());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());
        dto.setTransactionId(payment.getTransactionId());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
