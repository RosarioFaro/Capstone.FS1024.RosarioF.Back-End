package com.comicsdb.comics.publisher;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Optional<Publisher> findByComicVineId(Long comicVineId);
    boolean existsByComicVineId(Long comicVineId);
}