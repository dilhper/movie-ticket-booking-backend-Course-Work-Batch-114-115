package com.cinebook.moviebooking.dto.response;

import com.cinebook.moviebooking.entity.Movie;
import com.cinebook.moviebooking.entity.enums.MovieStatus;
import java.time.LocalDate;

public class MovieResponse {

    private Long id;
    private String title;
    private String description;
    private Integer duration;
    private String language;
    private String genre;
    private LocalDate releaseDate;
    private String posterUrl;
    private String director;
    private Double rating;
    private MovieStatus status;

    public MovieResponse() {
    }

    public static MovieResponse fromEntity(Movie movie) {
        if (movie == null) return null;
        MovieResponse dto = new MovieResponse();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDescription(movie.getDescription());
        dto.setDuration(movie.getDuration());
        dto.setLanguage(movie.getLanguage());
        dto.setGenre(movie.getGenre());
        dto.setReleaseDate(movie.getReleaseDate());
        dto.setPosterUrl(movie.getPosterUrl());
        dto.setDirector(movie.getDirector());
        dto.setRating(movie.getRating());
        dto.setStatus(movie.getStatus());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public MovieStatus getStatus() {
        return status;
    }

    public void setStatus(MovieStatus status) {
        this.status = status;
    }
}
