package com.cinebook.moviebooking.dto.response;

import com.cinebook.moviebooking.entity.Show;
import com.cinebook.moviebooking.entity.enums.ShowStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public class ShowResponse {

    private Long id;
    private Long movieId;
    private String movieTitle;
    private String moviePosterUrl;
    private Long theatreId;
    private String theatreName;
    private String theatreLocation;
    private LocalDate showDate;
    private LocalTime showTime;
    private Double ticketPrice;
    private String screen;
    private String format;
    private Integer rows;
    private Integer seatsPerRow;
    private Integer totalSeats;
    private Integer availableSeats;
    private ShowStatus status;

    public ShowResponse() {
    }

    public static ShowResponse fromEntity(Show show, int availableSeats) {
        if (show == null) return null;
        ShowResponse dto = new ShowResponse();
        dto.setId(show.getId());
        if (show.getMovie() != null) {
            dto.setMovieId(show.getMovie().getId());
            dto.setMovieTitle(show.getMovie().getTitle());
            dto.setMoviePosterUrl(show.getMovie().getPosterUrl());
        }
        if (show.getTheatre() != null) {
            dto.setTheatreId(show.getTheatre().getId());
            dto.setTheatreName(show.getTheatre().getName());
            dto.setTheatreLocation(show.getTheatre().getLocation());
        }
        dto.setShowDate(show.getShowDate());
        dto.setShowTime(show.getShowTime());
        dto.setTicketPrice(show.getTicketPrice());
        dto.setScreen(show.getScreen());
        dto.setFormat(show.getFormat());
        dto.setRows(show.getRows());
        dto.setSeatsPerRow(show.getSeatsPerRow());
        int total = (show.getRows() != null && show.getSeatsPerRow() != null)
                ? (show.getRows() * show.getSeatsPerRow())
                : (show.getTheatre() != null ? show.getTheatre().getCapacity() : 100);
        dto.setTotalSeats(total);
        dto.setAvailableSeats(availableSeats);
        dto.setStatus(show.getStatus());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMoviePosterUrl() {
        return moviePosterUrl;
    }

    public void setMoviePosterUrl(String moviePosterUrl) {
        this.moviePosterUrl = moviePosterUrl;
    }

    public Long getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(Long theatreId) {
        this.theatreId = theatreId;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    public String getTheatreLocation() {
        return theatreLocation;
    }

    public void setTheatreLocation(String theatreLocation) {
        this.theatreLocation = theatreLocation;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public void setShowDate(LocalDate showDate) {
        this.showDate = showDate;
    }

    public LocalTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalTime showTime) {
        this.showTime = showTime;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(Integer seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public ShowStatus getStatus() {
        return status;
    }

    public void setStatus(ShowStatus status) {
        this.status = status;
    }
}
