package com.cinebook.moviebooking.service.impl;

import com.cinebook.moviebooking.dto.request.MovieRequest;
import com.cinebook.moviebooking.dto.response.MovieResponse;
import com.cinebook.moviebooking.entity.Movie;
import com.cinebook.moviebooking.entity.enums.MovieStatus;
import com.cinebook.moviebooking.exception.ResourceNotFoundException;
import com.cinebook.moviebooking.repository.MovieRepository;
import com.cinebook.moviebooking.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    @Transactional
    public MovieResponse createMovie(MovieRequest request) {
        Movie movie = new Movie();
        mapRequestToEntity(request, movie);
        Movie savedMovie = movieRepository.save(movie);
        return MovieResponse.fromEntity(savedMovie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponse> getAllMovies(String title, String genre, String language, MovieStatus status) {
        List<Movie> movies = movieRepository.filterMovies(title, genre, language, status);
        return movies.stream()
                .map(MovieResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovieResponse> getMoviesPaginated(String title, String genre, String language, MovieStatus status, Pageable pageable) {
        Page<Movie> moviePage = movieRepository.searchMovies(title, genre, language, status, pageable);
        return moviePage.map(MovieResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public MovieResponse getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        return MovieResponse.fromEntity(movie);
    }

    @Override
    @Transactional
    public MovieResponse updateMovie(Long id, MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));

        mapRequestToEntity(request, movie);
        Movie updatedMovie = movieRepository.save(movie);
        return MovieResponse.fromEntity(updatedMovie);
    }

    @Override
    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        movieRepository.delete(movie);
    }

    private void mapRequestToEntity(MovieRequest request, Movie movie) {
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setDuration(request.getDuration());
        movie.setLanguage(request.getLanguage());
        movie.setGenre(request.getGenre());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setDirector(request.getDirector());
        if (request.getRating() != null) {
            movie.setRating(request.getRating());
        }
        if (request.getStatus() != null) {
            movie.setStatus(request.getStatus());
        }
    }
}
