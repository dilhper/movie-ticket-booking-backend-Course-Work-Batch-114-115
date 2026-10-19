package com.cinebook.moviebooking.dto.response;

import com.cinebook.moviebooking.entity.Theatre;
import com.cinebook.moviebooking.entity.enums.TheatreStatus;

public class TheatreResponse {

    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    private TheatreStatus status;
    private String contactNumber;
    private String facilities;

    public TheatreResponse() {
    }

    public static TheatreResponse fromEntity(Theatre theatre) {
        if (theatre == null) return null;
        TheatreResponse dto = new TheatreResponse();
        dto.setId(theatre.getId());
        dto.setName(theatre.getName());
        dto.setLocation(theatre.getLocation());
        dto.setCapacity(theatre.getCapacity());
        dto.setStatus(theatre.getStatus());
        dto.setContactNumber(theatre.getContactNumber());
        dto.setFacilities(theatre.getFacilities());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public TheatreStatus getStatus() {
        return status;
    }

    public void setStatus(TheatreStatus status) {
        this.status = status;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }
}
