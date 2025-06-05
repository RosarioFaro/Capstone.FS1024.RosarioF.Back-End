package com.comicsdb.comics.comments;

import lombok.Data;

@Data
public class CommentDto {
    private Long volumeId;
    private String username;
    private String text;
}