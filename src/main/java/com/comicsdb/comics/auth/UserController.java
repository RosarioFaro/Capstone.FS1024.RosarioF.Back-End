package com.comicsdb.comics.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final AppUserService appUserService;
    
    @GetMapping("/{username}")
    public ResponseEntity<AppUserDto> getUser(@PathVariable String username) {
        AppUser user = appUserService.loadUserByUsername(username);
        return ResponseEntity.ok(new AppUserDto(user.getUsername(), user.getAvatar()));
    }
}
