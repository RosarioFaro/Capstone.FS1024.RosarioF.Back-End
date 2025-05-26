package com.comicsdb.comics.volume;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/volumes")
@RequiredArgsConstructor
public class VolumeController {
    
    private final VolumeService volumeService;
    private final VolumeRepository volumeRepository;
    
    // --- GET paginato dai volumi nel database ---
    @GetMapping
    public ResponseEntity<Page<Volume>> getVolumi(
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        Page<Volume> result = (search != null && !search.isEmpty())
                ? volumeRepository.findByNameContainingIgnoreCase(search, pageable)
                : volumeRepository.findAll(pageable);
        return ResponseEntity.ok(result);
    }
    
    // --- Importa N volumi da ComicVine filtrati per publisher ---
    @PostMapping("/import/publisher/{publisherId}")
    public ResponseEntity<String> importVolumesByPublisher(
            @PathVariable int publisherId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        volumeService.fetchVolumesByPublisher(publisherId, page, size);
        return ResponseEntity.ok("Import volumi per publisher " + publisherId + " completata.");
    }
    
    // --- Ricerca su ComicVine: query per nome o per id volume ---
    @GetMapping("/search-comicvine")
    public ResponseEntity<?> searchComicVine(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String id
    ) {
        if (id != null && !id.isEmpty()) {
            return ResponseEntity.ok(volumeService.getComicVineVolumeById(id));
        }
        if (query != null && !query.isEmpty()) {
            return ResponseEntity.ok(volumeService.searchComicVineVolumes(query));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
    
    // --- GET dettaglio volume dal database ---
    @GetMapping("/{id}")
    public ResponseEntity<Volume> getVolumeById(@PathVariable Long id) {
        return volumeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
