package com.comicsdb.comics.volume;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/volumes")
@RequiredArgsConstructor
public class VolumeController {
    
    private final VolumeService volumeService;
    private final VolumeRepository volumeRepository;
    
    @GetMapping
    public ResponseEntity<Page<Volume>> getVolumi(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long publisher,
            Pageable pageable
    ) {
        Page<Volume> result;
        
        if (search != null && !search.isEmpty() && publisher != null) {
            result = volumeRepository.findByNameContainingIgnoreCaseAndPublisherId(search, publisher, pageable);
        } else if (search != null && !search.isEmpty()) {
            result = volumeRepository.findByNameContainingIgnoreCase(search, pageable);
        } else if (publisher != null) {
            result = volumeRepository.findByPublisherId(publisher, pageable);
        } else {
            result = volumeRepository.findAll(pageable);
        }
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/publishers")
    public ResponseEntity<List<Map<String, Object>>> getPublishers() {
        List<Map<String, Object>> publishers = volumeRepository.findDistinctPublishers();
        return ResponseEntity.ok(publishers);
    }
    
    @PostMapping("/import/publisher/{publisherId}")
    public ResponseEntity<String> importVolumesByPublisher(
            @PathVariable int publisherId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        volumeService.fetchVolumesByPublisher(publisherId, page, size);
        return ResponseEntity.ok("Import volumi per publisher " + publisherId + " completata.");
    }
    
    @GetMapping("/search-comicvine")
    public ResponseEntity<?> searchComicVine(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String id
    ) {
        if (id != null && !id.isEmpty()) {
            return ResponseEntity.ok(volumeService.getAndSaveComicVineVolumeById(id));
        }
        if (query != null && !query.isEmpty()) {
            return ResponseEntity.ok(volumeService.searchComicVineVolumes(query));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Volume> getVolumeById(@PathVariable Long id) {
        return volumeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/comicvine/{comicVineId}")
    public ResponseEntity<Volume> getOrImportByComicVineId(@PathVariable Long comicVineId) {
        Optional<Volume> local = volumeRepository.findByComicVineId(comicVineId);
        if (local.isPresent()) {
            return ResponseEntity.ok(local.get());
        }
        Volume imported = volumeService.getAndSaveComicVineVolumeById(comicVineId.toString());
        if (imported != null) {
            return ResponseEntity.ok(imported);
        }
        return ResponseEntity.notFound().build();
    }
}
