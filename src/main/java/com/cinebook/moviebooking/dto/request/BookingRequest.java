package com.cinebook.moviebooking.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class BookingRequest {

    @NotNull(message = "Show ID is required")
    private Long showId;

    @NotEmpty(message = "At least one seat must be selected")
    private List<String> seatNumbers;

    private Integer numberOfTickets;

    public BookingRequest() {
    }

    public BookingRequest(Long showId, List<String> seatNumbers, Integer numberOfTickets) {
        this.showId = showId;
        this.seatNumbers = seatNumbers;
        this.numberOfTickets = numberOfTickets;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public List<String> getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(List<String> seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public Integer getNumberOfTickets() {
        return numberOfTickets != null ? numberOfTickets : (seatNumbers != null ? seatNumbers.size() : 0);
    }

    public void setNumberOfTickets(Integer numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }
}
