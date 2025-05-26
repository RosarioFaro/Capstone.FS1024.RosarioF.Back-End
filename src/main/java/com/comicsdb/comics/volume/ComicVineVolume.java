package com.comicsdb.comics.volume;

import lombok.Data;

@Data
public class ComicVineVolume {
    private Long id;
    private String name;
    private String start_year;
    private int count_of_issues;
    private String  description;
    private String api_detail_url;
    private Image image;
    private Publisher publisher;
    
    @Data
    public static class Image {
        private String original_url;
    }
    
    @Data
    public static class Publisher {
        private Integer id;
        private String name;
    }
}
