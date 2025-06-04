package com.comicsdb.comics.volume.user_library;


import com.comicsdb.comics.auth.AppUser;
import com.comicsdb.comics.volume.Volume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserVolumeRepository extends JpaRepository<UserVolume, Long> {
    List<UserVolume> findByUser(AppUser user);
    Optional<UserVolume> findByUserAndVolume(AppUser user, Volume volume);
    void deleteByUserAndVolume(AppUser user, Volume volume);
    Optional<UserVolume> findByUserAndVolume_ComicVineId(AppUser user, Long comicVineId);
}