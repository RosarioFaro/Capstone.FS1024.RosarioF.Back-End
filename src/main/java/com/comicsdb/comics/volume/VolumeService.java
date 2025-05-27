package com.comicsdb.comics.volume;

import com.comicsdb.comics.publisher.PublisherDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class VolumeService {
    
    private final RestTemplate restTemplate;
    private final VolumeRepository volumeRepository;
    
    @Value("${comicvine.api.key}")
    private String apiKey;
    
    private static final int DESCRIPTION_MAX_LENGTH = 5000;
    
    public VolumeService(RestTemplate restTemplate, VolumeRepository volumeRepository) {
        this.restTemplate = restTemplate;
        this.volumeRepository = volumeRepository;
    }
    
    public Page<Volume> getAllVolumes(Pageable pageable) {
        return volumeRepository.findAll(pageable);
    }
    
    public void fetchVolumesByPublisher(int publisherId, int page, int size) {
        int offset = page * size;
        int fetchBatchSize = size * 5;
        
        String url = "https://comicvine.gamespot.com/api/volumes/?" +
                "api_key=" + apiKey +
                "&format=json" +
                "&sort=start_year:desc" +
                "&limit=" + fetchBatchSize +
                "&offset=" + offset;
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "MyComicListApp");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<ComicVineVolumesResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, ComicVineVolumesResponse.class
        );
        
        if (response.getBody() == null || response.getBody().getResults() == null) {
            System.out.println("Nessuna risposta valida dalla ComicVine.");
            return;
        }
        
        List<ComicVineVolume> volumes = response.getBody().getResults();
        System.out.println("Volumi ricevuti (batch): " + volumes.size());
        
        int imported = 0;
        for (ComicVineVolume v : volumes) {
            if (v.getName() == null || v.getPublisher() == null || v.getPublisher().getName() == null)
                continue;
            
            // FILTRO LATO JAVA: solo i volumi con il publisher desiderato
            if (!Objects.equals(v.getPublisher().getId(), publisherId))
                continue;
            
            Optional<Volume> existingOpt = volumeRepository.findByComicVineId(v.getId());
            
            if (existingOpt.isPresent()) {
                Volume existing = existingOpt.get();
                boolean changed = false;
                
                if (!Objects.equals(existing.getName(), v.getName())) {
                    existing.setName(v.getName());
                    changed = true;
                }
                if (!Objects.equals(existing.getStartYear(), v.getStart_year())) {
                    existing.setStartYear(v.getStart_year());
                    changed = true;
                }
                if (!Objects.equals(existing.getIssueCount(), v.getCount_of_issues())) {
                    existing.setIssueCount(v.getCount_of_issues());
                    changed = true;
                }
                String description = (v.getDescription());
                if (!Objects.equals(existing.getDescription(), description)) {
                    existing.setDescription(description);
                    changed = true;
                }
                if (!Objects.equals(existing.getPublisher(), v.getPublisher().getName())) {
                    existing.setPublisher(v.getPublisher().getName());
                    changed = true;
                }
                if (!Objects.equals(existing.getPublisherId(), v.getPublisher().getId())) {
                    existing.setPublisherId(v.getPublisher().getId());
                    changed = true;
                }
                String imageUrl = (v.getImage() != null ? v.getImage().getOriginal_url() : null);
                if (!Objects.equals(existing.getImageUrl(), imageUrl)) {
                    existing.setImageUrl(imageUrl);
                    changed = true;
                }
                if (!Objects.equals(existing.getApiDetailUrl(), v.getApi_detail_url())) {
                    existing.setApiDetailUrl(v.getApi_detail_url());
                    changed = true;
                }
                if (changed) {
                    volumeRepository.save(existing);
                    System.out.println("Aggiornato: " + existing.getName());
                }
            } else {
                Volume volume = toVolume(v);
                volumeRepository.save(volume);
                System.out.println("Inserito: " + v.getName());
            }
            
            imported++;
            if (imported >= size) break; // Limiti a 'size' i volumi effettivamente processati
        }
        System.out.println("Volumi importati per il publisher: " + imported);
    }
    
    public List<Volume> searchComicVineVolumes(String query) {
        String url = "https://comicvine.gamespot.com/api/volumes/?" +
                "api_key=" + apiKey +
                "&format=json" +
                "&filter=name:" + query +
                "&limit=10";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "TheComicsVault");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<ComicVineVolumesResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, ComicVineVolumesResponse.class
        );
        
        List<ComicVineVolume> volumes = (response.getBody() != null && response.getBody().getResults() != null)
                ? response.getBody().getResults()
                : List.of();
        
        return volumes.stream().map(this::toVolume).toList();
    }
    
    public Volume getComicVineVolumeById(String id) {
        String url = "https://comicvine.gamespot.com/api/volume/4050-" + id + "/?api_key=" + apiKey + "&format=json";
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "TheComicsVault");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<ComicVineVolumeResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, ComicVineVolumeResponse.class
        );
        ComicVineVolume v = response.getBody() != null ? response.getBody().getResults() : null;
        if (v == null) return null;
        return toVolume(v);
    }
    
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
    
    public Volume getAndSaveComicVineVolumeById(String comicVineId) {
        Volume volume = getComicVineVolumeById(comicVineId);
        System.out.println("Volume trovato da ComicVine: " + (volume != null ? volume.getName() : "null"));
        if (volume == null) return null;
        if (volumeRepository.existsByComicVineId(volume.getComicVineId())) {
            System.out.println("Volume già esistente in DB!");
            return volumeRepository.findByComicVineId(volume.getComicVineId()).orElse(volume);
        }
        Volume saved = volumeRepository.save(volume);
        System.out.println("Volume salvato! ID DB: " + saved.getId() + ", ComicVineId: " + saved.getComicVineId());
        return saved;
    }
    
    private Volume toVolume(ComicVineVolume v) {
        return Volume.builder()
                .comicVineId(v.getId())
                .name(v.getName())
                .startYear(v.getStart_year())
                .issueCount(v.getCount_of_issues())
                .description(v.getDescription())
                .publisher(v.getPublisher() != null ? v.getPublisher().getName() : "")
                .publisherId(v.getPublisher() != null ? v.getPublisher().getId() : null)
                .imageUrl(v.getImage() != null ? v.getImage().getOriginal_url() : null)
                .apiDetailUrl(v.getApi_detail_url())
                .build();
    }
    
}
