package com.comicsdb.comics.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppUserDto {
    private String username;
    private String avatar;
}
