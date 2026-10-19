package com.cinebook.moviebooking.service;

import com.cinebook.moviebooking.dto.request.MovieRequest;
import com.cinebook.moviebooking.dto.response.MovieResponse;
import com.cinebook.moviebooking.entity.enums.MovieStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {

    MovieResponse createMovie(MovieRequest request);

    List<MovieResponse> getAllMovies(String title, String genre, String language, MovieStatus status);

    Page<MovieResponse> getMoviesPaginated(String title, String genre, String language, MovieStatus status, Pageable pageable);

    MovieResponse getMovieById(Long id);

    MovieResponse updateMovie(Long id, MovieRequest request);

    void deleteMovie(Long id);
}
