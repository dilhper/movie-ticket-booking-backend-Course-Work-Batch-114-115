package com.cinebook.moviebooking.service;

import com.cinebook.moviebooking.dto.request.TheatreRequest;
import com.cinebook.moviebooking.dto.response.TheatreResponse;
import com.cinebook.moviebooking.entity.enums.TheatreStatus;

import java.util.List;

public interface TheatreService {

    TheatreResponse createTheatre(TheatreRequest request);

    List<TheatreResponse> getAllTheatres(String location, TheatreStatus status);

    TheatreResponse getTheatreById(Long id);

    TheatreResponse updateTheatre(Long id, TheatreRequest request);

    void deleteTheatre(Long id);
}
