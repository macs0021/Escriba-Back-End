package com.marco.appEscritura.dto;

import com.marco.appEscritura.entity.Document;
import lombok.Data;

@Data
public class DocumentDTO {

    long id;
    String tittle;
    String cover;
    String privateText;

    String creatorUsername;
    String synopsis;

    public DocumentDTO() {
        this.privateText = "";
    }
    public DocumentDTO(long id, String privateText) {
        this.id = id;
        this.privateText = privateText;
    }

    public DocumentDTO(long id, String tittle, String cover, String privateText, String creatorUsername, String synopsis) {
        this.id = id;
        this.tittle = tittle;
        this.cover = cover;
        this.privateText = privateText;
        this.creatorUsername = creatorUsername;
        this.synopsis = synopsis;
    }
}
