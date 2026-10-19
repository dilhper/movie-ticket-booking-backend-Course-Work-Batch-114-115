package com.cinebook.moviebooking.repository;

import com.cinebook.moviebooking.entity.Theatre;
import com.cinebook.moviebooking.entity.enums.TheatreStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {

    List<Theatre> findByStatus(TheatreStatus status);

    List<Theatre> findByLocationContainingIgnoreCase(String location);
}
