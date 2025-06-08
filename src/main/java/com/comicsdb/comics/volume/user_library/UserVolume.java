package com.comicsdb.comics.volume.user_library;

import com.comicsdb.comics.auth.AppUser;
import com.comicsdb.comics.volume.Volume;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_volumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVolume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
    
    @Column(name = "comicvine_id")
    private Long comicVineId;
    
    @ManyToOne(optional = false)
    private Volume volume;
    
    @Enumerated(EnumType.STRING)
    private ReadingStatus status;
    
    private Integer userScore;
    private Integer currentIssue;
    private Integer issueCount;
}
