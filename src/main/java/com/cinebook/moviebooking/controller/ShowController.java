package com.cinebook.moviebooking.controller;

import com.cinebook.moviebooking.dto.request.ShowRequest;
import com.cinebook.moviebooking.dto.response.ApiResponse;
import com.cinebook.moviebooking.dto.response.SeatAvailabilityResponse;
import com.cinebook.moviebooking.dto.response.ShowResponse;
import com.cinebook.moviebooking.entity.enums.ShowStatus;
import com.cinebook.moviebooking.service.ShowService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ShowResponse>>> getAllShows(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) Long theatreId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate showDate,
            @RequestParam(required = false) ShowStatus status
    ) {
        List<ShowResponse> shows = showService.getAllShows(movieId, theatreId, showDate, status);
        return ResponseEntity.ok(ApiResponse.success("Shows retrieved successfully", shows));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShowResponse>> getShowById(@PathVariable Long id) {
        ShowResponse show = showService.getShowById(id);
        return ResponseEntity.ok(ApiResponse.success("Show retrieved successfully", show));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<ApiResponse<List<ShowResponse>>> getShowsByMovieId(@PathVariable Long movieId) {
        List<ShowResponse> shows = showService.getShowsByMovieId(movieId);
        return ResponseEntity.ok(ApiResponse.success("Shows for movie retrieved successfully", shows));
    }

    @GetMapping("/{id}/available-seats")
    public ResponseEntity<ApiResponse<SeatAvailabilityResponse>> getAvailableSeats(@PathVariable Long id) {
        SeatAvailabilityResponse availability = showService.getAvailableSeats(id);
        return ResponseEntity.ok(ApiResponse.success("Seat availability retrieved successfully", availability));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ShowResponse>> createShow(@Valid @RequestBody ShowRequest request) {
        ShowResponse created = showService.createShow(request);
        return new ResponseEntity<>(
                ApiResponse.success("Show created successfully", created),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ShowResponse>> updateShow(
            @PathVariable Long id,
            @Valid @RequestBody ShowRequest request
    ) {
        ShowResponse updated = showService.updateShow(id, request);
        return ResponseEntity.ok(ApiResponse.success("Show updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return ResponseEntity.ok(ApiResponse.success("Show deleted successfully", null));
    }
}
