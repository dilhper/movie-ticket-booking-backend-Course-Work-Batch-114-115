package com.cinebook.moviebooking.controller;

import com.cinebook.moviebooking.dto.request.TheatreRequest;
import com.cinebook.moviebooking.dto.response.ApiResponse;
import com.cinebook.moviebooking.dto.response.TheatreResponse;
import com.cinebook.moviebooking.entity.enums.TheatreStatus;
import com.cinebook.moviebooking.service.TheatreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatres")
public class TheatreController {

    private final TheatreService theatreService;

    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TheatreResponse>>> getAllTheatres(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) TheatreStatus status
    ) {
        List<TheatreResponse> theatres = theatreService.getAllTheatres(location, status);
        return ResponseEntity.ok(ApiResponse.success("Theatres retrieved successfully", theatres));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TheatreResponse>> getTheatreById(@PathVariable Long id) {
        TheatreResponse theatre = theatreService.getTheatreById(id);
        return ResponseEntity.ok(ApiResponse.success("Theatre retrieved successfully", theatre));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TheatreResponse>> createTheatre(@Valid @RequestBody TheatreRequest request) {
        TheatreResponse created = theatreService.createTheatre(request);
        return new ResponseEntity<>(
                ApiResponse.success("Theatre created successfully", created),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TheatreResponse>> updateTheatre(
            @PathVariable Long id,
            @Valid @RequestBody TheatreRequest request
    ) {
        TheatreResponse updated = theatreService.updateTheatre(id, request);
        return ResponseEntity.ok(ApiResponse.success("Theatre updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTheatre(@PathVariable Long id) {
        theatreService.deleteTheatre(id);
        return ResponseEntity.ok(ApiResponse.success("Theatre deleted successfully", null));
    }
}
