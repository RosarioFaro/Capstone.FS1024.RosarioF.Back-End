package com.comicsdb.comics.volume;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VolumeRepository extends JpaRepository<Volume, Long> {
    boolean existsByNameAndStartYear(String name, String startYear);
    
    boolean existsByComicVineId(Long comicVineId);
    Optional<Volume> findByComicVineId(Long comicVineId);
    
    Page<Volume> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
