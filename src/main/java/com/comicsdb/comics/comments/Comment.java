package com.comicsdb.comics.comments;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")

public class Comment {
    @Id
    @GeneratedValue
    private Long id;
    private Long volumeId;
    private String username;
    private String text;
    private LocalDateTime timestamp;
}