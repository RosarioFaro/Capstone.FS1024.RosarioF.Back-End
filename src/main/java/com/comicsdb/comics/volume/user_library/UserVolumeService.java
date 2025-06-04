package com.comicsdb.comics.volume.user_library;

import com.comicsdb.comics.auth.AppUser;
import com.comicsdb.comics.volume.Volume;
import com.comicsdb.comics.volume.VolumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserVolumeService {
    
    private final UserVolumeRepository userVolumeRepository;
    private final VolumeRepository volumeRepository;
    
    public List<UserVolume> getLibraryForUser(AppUser user) {
        return userVolumeRepository.findByUser(user);
    }
    
    public Optional<UserVolume> getUserVolume(AppUser user, Long comicVineId) {
        return userVolumeRepository.findByUserAndVolume_ComicVineId(user, comicVineId);
    }
    
    public UserVolume addOrUpdateEntry(AppUser user, Long comicVineId, ReadingStatus status, Integer score, Integer issue) {
        Volume volume = volumeRepository.findByComicVineId(comicVineId)
                .orElseThrow(() -> new IllegalArgumentException("Volume non trovato"));
        
        Optional<UserVolume> opt = userVolumeRepository.findByUserAndVolume(user, volume);
        
        UserVolume entry = opt.orElse(
                UserVolume.builder()
                        .user(user)
                        .volume(volume)
                        .build()
        );
        
        entry.setStatus(status);
        entry.setUserScore(score);
        entry.setCurrentIssue(issue);
        
        return userVolumeRepository.save(entry);
    }
    
    public void deleteEntry(AppUser user, Long entryId) {
        UserVolume entry = userVolumeRepository.findById(entryId)
                .orElseThrow(() -> new IllegalArgumentException("Entry non trovata"));
        
        if (!entry.getUser().getId().equals(user.getId()))
            throw new SecurityException("Accesso negato");
        
        userVolumeRepository.delete(entry);
    }
}

