package com.comicsdb.comics.volume.user_library;

import com.comicsdb.comics.volume.user_library.ReadingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVolumeDto {
    private Long id;
    private Long comicVineId;
    private String volumeName;
    private ReadingStatus status;
    private Integer userScore;
    private Integer currentIssue;
    private Integer issueCount;
}
