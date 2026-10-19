package com.cinebook.moviebooking.service;

import com.cinebook.moviebooking.dto.request.ShowRequest;
import com.cinebook.moviebooking.dto.response.SeatAvailabilityResponse;
import com.cinebook.moviebooking.dto.response.ShowResponse;
import com.cinebook.moviebooking.entity.enums.ShowStatus;

import java.time.LocalDate;
import java.util.List;

public interface ShowService {

    ShowResponse createShow(ShowRequest request);

    List<ShowResponse> getAllShows(Long movieId, Long theatreId, LocalDate showDate, ShowStatus status);

    ShowResponse getShowById(Long id);

    List<ShowResponse> getShowsByMovieId(Long movieId);

    SeatAvailabilityResponse getAvailableSeats(Long showId);

    ShowResponse updateShow(Long id, ShowRequest request);

    void deleteShow(Long id);
}
