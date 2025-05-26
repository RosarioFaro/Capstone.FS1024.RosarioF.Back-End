package com.comicsdb.comics.publisher;

import com.comicsdb.comics.volume.VolumeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/publisher")
public class PublisherController {
    
    private final VolumeService volumeService;
    
    public PublisherController(VolumeService volumeService) {
        this.volumeService = volumeService;
    }
    
    @GetMapping("/{id}")
    public PublisherDto getPublisher(@PathVariable Integer id) {
        return volumeService.getPublisherById(id);
    }
}

