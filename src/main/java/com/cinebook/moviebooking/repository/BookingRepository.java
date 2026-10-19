package com.cinebook.moviebooking.repository;

import com.cinebook.moviebooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserIdOrderByBookingDateDesc(Long userId);

    List<Booking> findByShowId(Long showId);

    @Query("SELECT s FROM Booking b JOIN b.seatNumbers s WHERE b.show.id = :showId AND b.status != com.cinebook.moviebooking.entity.enums.BookingStatus.CANCELLED")
    List<String> findBookedSeatsByShowId(@Param("showId") Long showId);
}
