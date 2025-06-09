package com.comicsdb.comics.volume;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface VolumeRepository extends JpaRepository<Volume, Long> {
    boolean existsByNameAndStartYear(String name, String startYear);
    
    boolean existsByComicVineId(Long comicVineId);
    Optional<Volume> findByComicVineId(Long comicVineId);
    Page<Volume> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Volume> findByPublisherId(Long publisherId, Pageable pageable);
    Page<Volume> findByNameContainingIgnoreCaseAndPublisherId(String name, Long publisherId, Pageable pageable);
    List<Volume> findByComicVineIdIn(List<Long> comicVineIds);
    
    @Query("SELECT DISTINCT v.publisherId as id, v.publisher as name FROM Volume v WHERE v.publisherId IS NOT NULL")
    List<Map<String, Object>> findDistinctPublishers();
    
}
