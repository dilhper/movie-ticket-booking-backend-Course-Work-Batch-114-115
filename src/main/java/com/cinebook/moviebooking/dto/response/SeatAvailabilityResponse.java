package com.cinebook.moviebooking.dto.response;

import java.util.List;

public class SeatAvailabilityResponse {

    private Long showId;
    private Integer totalSeats;
    private Integer availableSeats;
    private List<String> bookedSeats;
    private Integer rows;
    private Integer seatsPerRow;

    public SeatAvailabilityResponse() {
    }

    public SeatAvailabilityResponse(Long showId, Integer totalSeats, Integer availableSeats, List<String> bookedSeats, Integer rows, Integer seatsPerRow) {
        this.showId = showId;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.bookedSeats = bookedSeats;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
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

    public List<String> getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(List<String> bookedSeats) {
        this.bookedSeats = bookedSeats;
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
}
