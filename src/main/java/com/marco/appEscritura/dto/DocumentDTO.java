package com.marco.appEscritura.dto;

import com.marco.appEscritura.entity.Document;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

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

    public DocumentDTO(long id, String tittle, String cover, String privateText, String creatorUsername, String synopsis, List<String> genres, List<String> savedBy, List<ReadingDTO> readings, boolean isPublic, int rating) {
        System.out.println(genres);
        this.id = id;
        this.tittle = tittle;
        this.cover = cover;
        this.text = privateText;
        this.creatorUsername = creatorUsername;
        this.synopsis = synopsis;
        this.genres = genres;
        this.savedBy = savedBy;
        this.readings = readings;
        this.isPublic = isPublic;
        this.rating = rating;
    }
}
