package com.comicsdb.comics.volume;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "volumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Volume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long comicVineId;
    
    private String name;
    private String startYear;
    private int issueCount;
    
    @Column(length = 5000)
    private String description;
    
    private String publisher;
    private Integer publisherId;
    private String imageUrl;
    private String apiDetailUrl;
}
