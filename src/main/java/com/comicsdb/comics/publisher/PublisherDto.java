package com.comicsdb.comics.publisher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherDto {
    private Integer id;
    private String name;
    private String description;
    private String imageUrl;
}
