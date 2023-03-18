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
    String privateText;

    String creatorUsername;
    String synopsis;
    List<String> genres;

    List<String> savedBy;

    public DocumentDTO() {
        this.privateText = "";
        genres = new ArrayList<>();
    }
    public DocumentDTO(long id, String privateText) {
        this.id = id;
        this.privateText = privateText;
    }

    public DocumentDTO(long id, String tittle, String cover, String privateText, String creatorUsername, String synopsis, List<String> genres, List<String> savedBy) {
        System.out.println(genres);
        this.id = id;
        this.tittle = tittle;
        this.cover = cover;
        this.privateText = privateText;
        this.creatorUsername = creatorUsername;
        this.synopsis = synopsis;
        this.genres = genres;
        this.savedBy = savedBy;
    }
}
