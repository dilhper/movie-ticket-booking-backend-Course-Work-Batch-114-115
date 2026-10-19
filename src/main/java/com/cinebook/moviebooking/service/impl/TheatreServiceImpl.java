package com.cinebook.moviebooking.service.impl;

import com.cinebook.moviebooking.dto.request.TheatreRequest;
import com.cinebook.moviebooking.dto.response.TheatreResponse;
import com.cinebook.moviebooking.entity.Theatre;
import com.cinebook.moviebooking.entity.enums.TheatreStatus;
import com.cinebook.moviebooking.exception.ResourceNotFoundException;
import com.cinebook.moviebooking.repository.TheatreRepository;
import com.cinebook.moviebooking.service.TheatreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;

    public TheatreServiceImpl(TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
    }

    @Override
    @Transactional
    public TheatreResponse createTheatre(TheatreRequest request) {
        Theatre theatre = new Theatre();
        mapRequestToEntity(request, theatre);
        Theatre savedTheatre = theatreRepository.save(theatre);
        return TheatreResponse.fromEntity(savedTheatre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TheatreResponse> getAllTheatres(String location, TheatreStatus status) {
        List<Theatre> theatres = theatreRepository.findAll();

        return theatres.stream()
                .filter(t -> location == null || location.trim().isEmpty() ||
                        t.getLocation().toLowerCase().contains(location.trim().toLowerCase()))
                .filter(t -> status == null || t.getStatus() == status)
                .map(TheatreResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TheatreResponse getTheatreById(Long id) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + id));
        return TheatreResponse.fromEntity(theatre);
    }

    @Override
    @Transactional
    public TheatreResponse updateTheatre(Long id, TheatreRequest request) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + id));

        mapRequestToEntity(request, theatre);
        Theatre updatedTheatre = theatreRepository.save(theatre);
        return TheatreResponse.fromEntity(updatedTheatre);
    }

    @Override
    @Transactional
    public void deleteTheatre(Long id) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + id));
        theatreRepository.delete(theatre);
    }

    private void mapRequestToEntity(TheatreRequest request, Theatre theatre) {
        theatre.setName(request.getName());
        theatre.setLocation(request.getLocation());
        theatre.setCapacity(request.getCapacity());
        if (request.getStatus() != null) {
            theatre.setStatus(request.getStatus());
        }
        theatre.setContactNumber(request.getContactNumber());
        theatre.setFacilities(request.getFacilities());
    }
}
