package com.cinebook.moviebooking.repository;

import com.cinebook.moviebooking.entity.Show;
import com.cinebook.moviebooking.entity.enums.ShowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByMovieId(Long movieId);

    List<Show> findByTheatreId(Long theatreId);

    List<Show> findByShowDate(LocalDate showDate);

    List<Show> findByMovieIdAndShowDate(Long movieId, LocalDate showDate);

    @Query("SELECT s FROM Show s WHERE " +
           "(:movieId IS NULL OR s.movie.id = :movieId) AND " +
           "(:theatreId IS NULL OR s.theatre.id = :theatreId) AND " +
           "(:showDate IS NULL OR s.showDate = :showDate) AND " +
           "(:status IS NULL OR s.status = :status)")
    List<Show> filterShows(
            @Param("movieId") Long movieId,
            @Param("theatreId") Long theatreId,
            @Param("showDate") LocalDate showDate,
            @Param("status") ShowStatus status
    );

    boolean existsByTheatreIdAndScreenAndShowDateAndShowTimeAndStatusNot(
            Long theatreId,
            String screen,
            LocalDate showDate,
            LocalTime showTime,
            ShowStatus status
    );
}
