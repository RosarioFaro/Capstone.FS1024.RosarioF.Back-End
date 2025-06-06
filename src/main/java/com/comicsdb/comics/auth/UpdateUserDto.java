package com.comicsdb.comics.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserDto {
    private String username;
    private String currentPassword;
    private String newPassword;
    private String avatar;
}
