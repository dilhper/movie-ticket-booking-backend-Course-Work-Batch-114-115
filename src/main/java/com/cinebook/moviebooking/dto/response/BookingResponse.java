package com.cinebook.moviebooking.dto.response;

import com.cinebook.moviebooking.entity.Booking;
import com.cinebook.moviebooking.entity.enums.BookingStatus;
import com.cinebook.moviebooking.entity.enums.PaymentStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class BookingResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long showId;
    private String movieTitle;
    private String theatreName;
    private String theatreLocation;
    private LocalDate showDate;
    private LocalTime showTime;
    private String screen;
    private String format;
    private List<String> seatNumbers;
    private Integer numberOfTickets;
    private Double totalAmount;
    private LocalDateTime bookingDate;
    private BookingStatus status;
    private PaymentStatus paymentStatus;
    private Long paymentId;

    public BookingResponse() {
    }

    public static BookingResponse fromEntity(Booking booking) {
        if (booking == null) return null;
        BookingResponse dto = new BookingResponse();
        dto.setId(booking.getId());
        if (booking.getUser() != null) {
            dto.setUserId(booking.getUser().getId());
            dto.setUserName(booking.getUser().getName());
            dto.setUserEmail(booking.getUser().getEmail());
        }
        if (booking.getShow() != null) {
            dto.setShowId(booking.getShow().getId());
            if (booking.getShow().getMovie() != null) {
                dto.setMovieTitle(booking.getShow().getMovie().getTitle());
            }
            if (booking.getShow().getTheatre() != null) {
                dto.setTheatreName(booking.getShow().getTheatre().getName());
                dto.setTheatreLocation(booking.getShow().getTheatre().getLocation());
            }
            dto.setShowDate(booking.getShow().getShowDate());
            dto.setShowTime(booking.getShow().getShowTime());
            dto.setScreen(booking.getShow().getScreen());
            dto.setFormat(booking.getShow().getFormat());
        }
        dto.setSeatNumbers(booking.getSeatNumbers());
        dto.setNumberOfTickets(booking.getNumberOfTickets());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setBookingDate(booking.getBookingDate());
        dto.setStatus(booking.getStatus());
        if (booking.getPayment() != null) {
            dto.setPaymentStatus(booking.getPayment().getStatus());
            dto.setPaymentId(booking.getPayment().getId());
        } else {
            dto.setPaymentStatus(PaymentStatus.PENDING);
        }
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    public String getTheatreLocation() {
        return theatreLocation;
    }

    public void setTheatreLocation(String theatreLocation) {
        this.theatreLocation = theatreLocation;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public void setShowDate(LocalDate showDate) {
        this.showDate = showDate;
    }

    public LocalTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalTime showTime) {
        this.showTime = showTime;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<String> getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(List<String> seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public Integer getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(Integer numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
}
