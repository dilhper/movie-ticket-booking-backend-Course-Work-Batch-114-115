package com.cinebook.moviebooking.service.impl;

import com.cinebook.moviebooking.dto.request.BookingRequest;
import com.cinebook.moviebooking.dto.response.BookingResponse;
import com.cinebook.moviebooking.entity.Booking;
import com.cinebook.moviebooking.entity.Show;
import com.cinebook.moviebooking.entity.User;
import com.cinebook.moviebooking.entity.enums.BookingStatus;
import com.cinebook.moviebooking.entity.enums.PaymentStatus;
import com.cinebook.moviebooking.entity.enums.ShowStatus;
import com.cinebook.moviebooking.exception.BadRequestException;
import com.cinebook.moviebooking.exception.ConflictException;
import com.cinebook.moviebooking.exception.ResourceNotFoundException;
import com.cinebook.moviebooking.exception.UnauthorizedException;
import com.cinebook.moviebooking.repository.BookingRepository;
import com.cinebook.moviebooking.repository.ShowRepository;
import com.cinebook.moviebooking.repository.UserRepository;
import com.cinebook.moviebooking.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            ShowRepository showRepository,
            UserRepository userRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.showRepository = showRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + request.getShowId()));

        if (show.getStatus() == ShowStatus.CANCELLED || show.getStatus() == ShowStatus.COMPLETED) {
            throw new BadRequestException("Cannot book tickets for a show with status " + show.getStatus());
        }

        List<String> requestedSeats = request.getSeatNumbers();
        if (requestedSeats == null || requestedSeats.isEmpty()) {
            throw new BadRequestException("At least one seat number must be specified");
        }

        // Check for intra-request seat duplicates
        Set<String> uniqueSeats = new HashSet<>(requestedSeats);
        if (uniqueSeats.size() != requestedSeats.size()) {
            throw new BadRequestException("Duplicate seat numbers detected in the booking request");
        }

        // Check for seat collision with already booked seats
        List<String> alreadyBookedSeats = bookingRepository.findBookedSeatsByShowId(show.getId());
        List<String> collidingSeats = requestedSeats.stream()
                .filter(alreadyBookedSeats::contains)
                .collect(Collectors.toList());

        if (!collidingSeats.isEmpty()) {
            throw new ConflictException("The following seat(s) are already booked: " + String.join(", ", collidingSeats));
        }

        int ticketCount = request.getNumberOfTickets();
        if (ticketCount <= 0 || ticketCount != requestedSeats.size()) {
            ticketCount = requestedSeats.size();
        }

        double totalAmount = ticketCount * show.getTicketPrice();

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setSeatNumbers(new ArrayList<>(requestedSeats));
        booking.setNumberOfTickets(ticketCount);
        booking.setTotalAmount(totalAmount);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);
        return BookingResponse.fromEntity(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id, String currentUserEmail, boolean isAdmin) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (!isAdmin && !booking.getUser().getEmail().equalsIgnoreCase(currentUserEmail)) {
            throw new UnauthorizedException("You are not authorized to view this booking");
        }

        return BookingResponse.fromEntity(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getUserBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        List<Booking> bookings = bookingRepository.findByUserIdOrderByBookingDateDesc(user.getId());
        return bookings.stream()
                .map(BookingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(BookingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long id, String currentUserEmail, boolean isAdmin) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (!isAdmin && !booking.getUser().getEmail().equalsIgnoreCase(currentUserEmail)) {
            throw new UnauthorizedException("You are not authorized to cancel this booking");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        // If payment was completed, mark it as REFUNDED
        if (booking.getPayment() != null && booking.getPayment().getStatus() == PaymentStatus.SUCCESS) {
            booking.getPayment().setStatus(PaymentStatus.REFUNDED);
        }

        Booking updatedBooking = bookingRepository.save(booking);
        return BookingResponse.fromEntity(updatedBooking);
    }
}
