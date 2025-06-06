package com.comicsdb.comics.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final AppUserService appUserService;
    
    @PutMapping("/{username}")
    public ResponseEntity<AppUserDto> updateUser(
            @PathVariable String username,
            @RequestBody UpdateUserDto updateUserDto) {
        AppUser updated = appUserService.updateUser(username, updateUserDto);
        return ResponseEntity.ok(new AppUserDto(updated.getUsername(), updated.getAvatar()));
    }
    
    @GetMapping("/{username}")
    public ResponseEntity<AppUserDto> getUser(@PathVariable String username) {
        AppUser user = appUserService.loadUserByUsername(username);
        return ResponseEntity.ok(new AppUserDto(user.getUsername(), user.getAvatar()));
    }
}

