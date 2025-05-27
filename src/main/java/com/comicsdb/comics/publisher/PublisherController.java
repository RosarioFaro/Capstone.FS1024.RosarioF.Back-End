package com.comicsdb.comics.publisher;

import com.comicsdb.comics.volume.VolumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/publisher")
public class PublisherController {
    
    private final VolumeService volumeService;
    private final PublisherService publisherService;
    private final PublisherRepository publisherRepository;
    
    @GetMapping("/{id}")
    public ResponseEntity<Publisher> getPublisherById(@PathVariable Long id) {
        return publisherRepository.findByComicVineId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/comicvine/{comicVineId}")
    public ResponseEntity<Publisher> getOrImportPublisherByComicVineId(@PathVariable Long comicVineId) {
        Optional<Publisher> local = publisherRepository.findByComicVineId(comicVineId);
        if (local.isPresent()) {
            return ResponseEntity.ok(local.get());
        }
        Publisher imported = publisherService.getAndSaveComicVinePublisherById(comicVineId.toString());
        if (imported != null) {
            return ResponseEntity.ok(imported);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/search-comicvine")
    public ResponseEntity<Publisher> searchAndImportFromComicVine(@RequestParam String id) {
        System.out.println("[DEBUG] Controller chiamato con id = " + id);
        Publisher publisher = publisherService.getAndSaveComicVinePublisherById(id);
        System.out.println("[DEBUG] Dopo chiamata service, publisher = " + publisher);
        if (publisher != null) {
            return ResponseEntity.ok(publisher);
        }
        return ResponseEntity.notFound().build();
    }
}
