package com.marco.EscribaServer.dto;

import com.marco.EscribaServer.entity.Genre;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class DocumentDTO {
    long id;
    String tittle;
    String cover;
    String text;
    boolean isPublic;
    String creatorUsername;
    String synopsis;
    List<String> genres;
    List<String> savedBy;
    List<ReadingDTO> readings;
    int rating;

    public DocumentDTO() {
        this.text = "";
        genres = new ArrayList<>();
    }
    public DocumentDTO(long id, String text) {
        this.id = id;
        this.text = text;
        this.rating = 0;
    }

    public DocumentDTO(long id, String tittle, String cover, String privateText, String creatorUsername, String synopsis, List<Genre> genres, List<String> savedBy, List<ReadingDTO> readings, boolean isPublic, int rating) {
        this.id = id;
        this.tittle = tittle;
        this.cover = cover;
        this.text = privateText;
        this.creatorUsername = creatorUsername;
        this.synopsis = synopsis;
        this.genres = genres.stream().map(Genre::getGenre).collect(Collectors.toList());
        this.savedBy = savedBy;
        this.readings = readings;
        this.isPublic = isPublic;
        this.rating = rating;
    }
}
