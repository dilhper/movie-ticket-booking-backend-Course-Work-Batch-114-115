package com.cinebook.moviebooking.service.impl;

import com.cinebook.moviebooking.dto.request.ShowRequest;
import com.cinebook.moviebooking.dto.response.SeatAvailabilityResponse;
import com.cinebook.moviebooking.dto.response.ShowResponse;
import com.cinebook.moviebooking.entity.Movie;
import com.cinebook.moviebooking.entity.Show;
import com.cinebook.moviebooking.entity.Theatre;
import com.cinebook.moviebooking.entity.enums.MovieStatus;
import com.cinebook.moviebooking.entity.enums.ShowStatus;
import com.cinebook.moviebooking.entity.enums.TheatreStatus;
import com.cinebook.moviebooking.exception.BadRequestException;
import com.cinebook.moviebooking.exception.ConflictException;
import com.cinebook.moviebooking.exception.ResourceNotFoundException;
import com.cinebook.moviebooking.repository.BookingRepository;
import com.cinebook.moviebooking.repository.MovieRepository;
import com.cinebook.moviebooking.repository.ShowRepository;
import com.cinebook.moviebooking.repository.TheatreRepository;
import com.cinebook.moviebooking.service.ShowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final BookingRepository bookingRepository;

    public ShowServiceImpl(
            ShowRepository showRepository,
            MovieRepository movieRepository,
            TheatreRepository theatreRepository,
            BookingRepository bookingRepository
    ) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional
    public ShowResponse createShow(ShowRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + request.getMovieId()));

        if (movie.getStatus() == MovieStatus.ENDED) {
            throw new BadRequestException("Cannot schedule show for movie with status ENDED");
        }

        Theatre theatre = theatreRepository.findById(request.getTheatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + request.getTheatreId()));

        if (theatre.getStatus() == TheatreStatus.INACTIVE) {
            throw new BadRequestException("Cannot schedule show in inactive theatre");
        }

        String screen = (request.getScreen() != null && !request.getScreen().trim().isEmpty())
                ? request.getScreen().trim() : "Screen 1";

        boolean conflict = showRepository.existsByTheatreIdAndScreenAndShowDateAndShowTimeAndStatusNot(
                theatre.getId(),
                screen,
                request.getShowDate(),
                request.getShowTime(),
                ShowStatus.CANCELLED
        );

        if (conflict) {
            throw new ConflictException("A show is already scheduled in " + theatre.getName() + " on " + screen + " at " + request.getShowDate() + " " + request.getShowTime());
        }

        Show show = new Show();
        show.setMovie(movie);
        show.setTheatre(theatre);
        show.setShowDate(request.getShowDate());
        show.setShowTime(request.getShowTime());
        show.setTicketPrice(request.getTicketPrice());
        show.setScreen(screen);
        show.setFormat(request.getFormat() != null ? request.getFormat() : "2D");
        show.setRows(request.getRows() != null ? request.getRows() : 8);
        show.setSeatsPerRow(request.getSeatsPerRow() != null ? request.getSeatsPerRow() : 12);
        show.setStatus(request.getStatus() != null ? request.getStatus() : ShowStatus.SCHEDULED);

        Show savedShow = showRepository.save(show);
        int availableSeats = computeAvailableSeats(savedShow);
        return ShowResponse.fromEntity(savedShow, availableSeats);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowResponse> getAllShows(Long movieId, Long theatreId, LocalDate showDate, ShowStatus status) {
        List<Show> shows = showRepository.filterShows(movieId, theatreId, showDate, status);
        return shows.stream()
                .map(s -> ShowResponse.fromEntity(s, computeAvailableSeats(s)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ShowResponse getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + id));
        return ShowResponse.fromEntity(show, computeAvailableSeats(show));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowResponse> getShowsByMovieId(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new ResourceNotFoundException("Movie not found with id: " + movieId);
        }
        List<Show> shows = showRepository.findByMovieId(movieId);
        return shows.stream()
                .map(s -> ShowResponse.fromEntity(s, computeAvailableSeats(s)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SeatAvailabilityResponse getAvailableSeats(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + showId));

        int rows = show.getRows() != null ? show.getRows() : 8;
        int seatsPerRow = show.getSeatsPerRow() != null ? show.getSeatsPerRow() : 12;
        int totalSeats = rows * seatsPerRow;

        List<String> bookedSeats = bookingRepository.findBookedSeatsByShowId(showId);
        int available = Math.max(0, totalSeats - bookedSeats.size());

        return new SeatAvailabilityResponse(showId, totalSeats, available, bookedSeats, rows, seatsPerRow);
    }

    @Override
    @Transactional
    public ShowResponse updateShow(Long id, ShowRequest request) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + id));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + request.getMovieId()));

        if (movie.getStatus() == MovieStatus.ENDED) {
            throw new BadRequestException("Cannot schedule show for movie with status ENDED");
        }

        Theatre theatre = theatreRepository.findById(request.getTheatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + request.getTheatreId()));

        if (theatre.getStatus() == TheatreStatus.INACTIVE) {
            throw new BadRequestException("Cannot schedule show in inactive theatre");
        }

        String screen = (request.getScreen() != null && !request.getScreen().trim().isEmpty())
                ? request.getScreen().trim() : show.getScreen();

        // Check if conflict with another show
        boolean timeOrPlaceChanged = !show.getTheatre().getId().equals(theatre.getId()) ||
                !show.getScreen().equalsIgnoreCase(screen) ||
                !show.getShowDate().equals(request.getShowDate()) ||
                !show.getShowTime().equals(request.getShowTime());

        if (timeOrPlaceChanged) {
            boolean conflict = showRepository.existsByTheatreIdAndScreenAndShowDateAndShowTimeAndStatusNot(
                    theatre.getId(),
                    screen,
                    request.getShowDate(),
                    request.getShowTime(),
                    ShowStatus.CANCELLED
            );
            if (conflict) {
                throw new ConflictException("A show is already scheduled in " + theatre.getName() + " on " + screen + " at " + request.getShowDate() + " " + request.getShowTime());
            }
        }

        show.setMovie(movie);
        show.setTheatre(theatre);
        show.setShowDate(request.getShowDate());
        show.setShowTime(request.getShowTime());
        show.setTicketPrice(request.getTicketPrice());
        show.setScreen(screen);
        if (request.getFormat() != null) show.setFormat(request.getFormat());
        if (request.getRows() != null) show.setRows(request.getRows());
        if (request.getSeatsPerRow() != null) show.setSeatsPerRow(request.getSeatsPerRow());
        if (request.getStatus() != null) show.setStatus(request.getStatus());

        Show updatedShow = showRepository.save(show);
        return ShowResponse.fromEntity(updatedShow, computeAvailableSeats(updatedShow));
    }

    @Override
    @Transactional
    public void deleteShow(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + id));
        showRepository.delete(show);
    }

    private int computeAvailableSeats(Show show) {
        int rows = show.getRows() != null ? show.getRows() : 8;
        int seatsPerRow = show.getSeatsPerRow() != null ? show.getSeatsPerRow() : 12;
        int totalSeats = rows * seatsPerRow;
        List<String> bookedSeats = bookingRepository.findBookedSeatsByShowId(show.getId());
        return Math.max(0, totalSeats - bookedSeats.size());
    }
}
