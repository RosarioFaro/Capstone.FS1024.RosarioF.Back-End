package com.comicsdb.comics.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PublisherService {
    
    @Value("${comicvine.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate;
    private final PublisherRepository publisherRepository;
    
    public PublisherDto getPublisherById(Integer id) {
        String url = "https://comicvine.gamespot.com/api/publisher/4010-" + id + "/?api_key=" + apiKey + "&format=json";
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "TheComicsVault");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, Map.class
        );
        
        Map body = response.getBody();
        if (body == null) return null;
        
        Map results = (Map) body.get("results");
        if (results == null) return null;
        
        PublisherDto dto = new PublisherDto();
        dto.setId((Integer) results.get("id"));
        dto.setName((String) results.get("name"));
        dto.setDescription((String) results.getOrDefault("description", ""));
        dto.setImageUrl((String) results.get("imageUrl"));
        
        Map image = (Map) results.get("image");
        if (image != null) {
            dto.setImageUrl((String) image.get("original_url"));
        }
        return dto;
    }
    
    public Publisher getAndSaveComicVinePublisherById(String comicVineId) {
        System.out.println(">>> getAndSaveComicVinePublisherById chiamato con id=" + comicVineId);
        PublisherDto publisherDto = getPublisherById(Integer.valueOf(comicVineId));
        if (publisherDto == null) {
            System.out.println(">>> publisherDto è NULL per id=" + comicVineId);
            return null;
        }
        
        Optional<Publisher> localOpt = publisherRepository.findByComicVineId(Long.valueOf(comicVineId));
        if (localOpt.isPresent()) {
            System.out.println(">>> Publisher già presente in DB!");
            return localOpt.get();
        }
        
        Publisher publisher = Publisher.builder()
                .comicVineId(Long.valueOf(comicVineId))
                .name(publisherDto.getName())
                .description(publisherDto.getDescription())
                .imageUrl(publisherDto.getImageUrl())
                .build();
        
        Publisher saved = publisherRepository.save(publisher);
        System.out.println(">>> Publisher salvato in DB! id=" + saved.getId() + ", nome=" + saved.getName());
        return saved;
    }
    
}
