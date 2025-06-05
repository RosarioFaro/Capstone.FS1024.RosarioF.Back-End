package com.comicsdb.comics.volume.user_library;

import com.comicsdb.comics.volume.user_library.UserVolume;
import com.comicsdb.comics.volume.user_library.UserVolumeDto;

public class UserVolumeMapper {
    
    public static UserVolumeDto toDto(UserVolume entity) {
        if (entity == null) return null;
        return new UserVolumeDto(
                entity.getId(),
                entity.getVolume().getComicVineId(),
                entity.getVolume().getName(),
                entity.getStatus(),
                entity.getUserScore(),
                entity.getCurrentIssue()
        );
    }
}

