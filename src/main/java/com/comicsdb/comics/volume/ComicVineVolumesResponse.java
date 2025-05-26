package com.comicsdb.comics.volume;

import lombok.Data;

import java.util.List;

@Data
public class ComicVineVolumesResponse {
    private List<ComicVineVolume> results;
}
