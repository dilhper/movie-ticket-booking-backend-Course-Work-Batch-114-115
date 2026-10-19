package com.cinebook.moviebooking.controller;

import com.cinebook.moviebooking.dto.request.MovieRequest;
import com.cinebook.moviebooking.dto.response.ApiResponse;
import com.cinebook.moviebooking.dto.response.MovieResponse;
import com.cinebook.moviebooking.entity.enums.MovieStatus;
import com.cinebook.moviebooking.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getAllMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) MovieStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        if (page != null) {
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<MovieResponse> moviePage = movieService.getMoviesPaginated(title, genre, language, status, pageable);
            return ResponseEntity.ok(ApiResponse.success("Movies retrieved successfully", moviePage));
        }

        List<MovieResponse> movies = movieService.getAllMovies(title, genre, language, status);
        return ResponseEntity.ok(ApiResponse.success("Movies retrieved successfully", movies));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovieResponse>> getMovieById(@PathVariable Long id) {
        MovieResponse movie = movieService.getMovieById(id);
        return ResponseEntity.ok(ApiResponse.success("Movie retrieved successfully", movie));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MovieResponse>> createMovie(@Valid @RequestBody MovieRequest request) {
        MovieResponse created = movieService.createMovie(request);
        return new ResponseEntity<>(
                ApiResponse.success("Movie created successfully", created),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MovieResponse>> updateMovie(
            @PathVariable Long id,
            @Valid @RequestBody MovieRequest request
    ) {
        MovieResponse updated = movieService.updateMovie(id, request);
        return ResponseEntity.ok(ApiResponse.success("Movie updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok(ApiResponse.success("Movie deleted successfully", null));
    }
}
