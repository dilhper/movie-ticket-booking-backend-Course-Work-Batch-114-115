package com.cinebook.moviebooking.entity;

import com.cinebook.moviebooking.entity.enums.TheatreStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "theatres")
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(nullable = false)
    private Integer capacity = 100;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TheatreStatus status = TheatreStatus.ACTIVE;

    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @Column(length = 200)
    private String facilities;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Show> shows = new ArrayList<>();

    public Theatre() {
    }

    public Theatre(String name, String location, Integer capacity, TheatreStatus status, String contactNumber, String facilities) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.status = status;
        this.contactNumber = contactNumber;
        this.facilities = facilities;
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

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }
}
