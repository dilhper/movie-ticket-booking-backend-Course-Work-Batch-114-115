package com.cinebook.moviebooking.dto.request;

import com.cinebook.moviebooking.entity.enums.ShowStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class ShowRequest {

    @NotNull(message = "Movie ID is required")
    private Long movieId;

    @NotNull(message = "Theatre ID is required")
    private Long theatreId;

    @NotNull(message = "Show date is required")
    private LocalDate showDate;

    @NotNull(message = "Show time is required")
    private LocalTime showTime;

    @NotNull(message = "Ticket price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Ticket price must be greater than zero")
    private Double ticketPrice;

    private String screen = "Screen 1";
    private String format = "2D";
    private Integer rows = 8;
    private Integer seatsPerRow = 12;
    private ShowStatus status = ShowStatus.SCHEDULED;

    public ShowRequest() {
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(Long theatreId) {
        this.theatreId = theatreId;
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

    public ShowStatus getStatus() {
        return status;
    }

    public void setStatus(ShowStatus status) {
        this.status = status;
    }
}
