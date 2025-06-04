package com.comicsdb.comics.volume.user_library;

import com.comicsdb.comics.auth.AppUser;
import com.comicsdb.comics.volume.user_library.ReadingStatus;
import com.comicsdb.comics.volume.user_library.UserVolumeDto;
import com.comicsdb.comics.volume.user_library.UserVolumeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-library")
@RequiredArgsConstructor
public class UserVolumeController {
    
    private final UserVolumeService userVolumeService;
    
    @GetMapping
    public List<UserVolumeDto> getUserLibrary(@AuthenticationPrincipal AppUser user) {
        return userVolumeService.getLibraryForUser(user)
                .stream()
                .map(UserVolumeMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @GetMapping("/{comicVineId}")
    public ResponseEntity<UserVolumeDto> getUserVolume(
            @AuthenticationPrincipal AppUser user,
            @PathVariable Long comicVineId
    ) {
        return userVolumeService.getUserVolume(user, comicVineId)
                .map(UserVolumeMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<UserVolumeDto> addOrUpdateEntry(
            @AuthenticationPrincipal AppUser user,
            @RequestParam Long comicVineId,
            @RequestParam ReadingStatus status,
            @RequestParam(required = false) Integer score,
            @RequestParam(required = false) Integer issue
    ) {
        UserVolume entry = userVolumeService.addOrUpdateEntry(user, comicVineId, status, score, issue);
        return ResponseEntity.ok(UserVolumeMapper.toDto(entry));
    }
    
    @DeleteMapping("/{entryId}")
    public ResponseEntity<Void> deleteEntry(
            @AuthenticationPrincipal AppUser user,
            @PathVariable Long entryId
    ) {
        userVolumeService.deleteEntry(user, entryId);
        return ResponseEntity.noContent().build();
    }
}
