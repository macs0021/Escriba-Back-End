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

    public DocumentDTO() {
        this.text = "";
        genres = new ArrayList<>();
    }
    public DocumentDTO(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public DocumentDTO(long id, String tittle, String cover, String privateText, String creatorUsername, String synopsis, List<String> genres, List<String> savedBy, List<ReadingDTO> readings, boolean isPublic) {
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
    }
}
