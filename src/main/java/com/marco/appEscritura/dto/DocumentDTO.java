package com.marco.appEscritura.dto;

import com.marco.appEscritura.entity.Document;

public class DocumentDTO {

    long id;
    String tittle;
    String cover;
    String privateText;

    String synopsis;

    public DocumentDTO() {
        this.privateText = "";
    }
    public DocumentDTO(long id, String privateText) {
        this.id = id;
        this.privateText = privateText;
    }

    public DocumentDTO(long id, String tittle, String cover, String synopsis, String privateText) {
        this.id = id;
        this.tittle = tittle;
        this.cover = cover;
        this.privateText = privateText;
        this.synopsis = synopsis;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPrivateText() {
        return privateText;
    }

    public void setPrivateText(String privateText) {
        this.privateText = privateText;
    }
    public Document toDocument(){
        Document document = new Document();
        document.setId(this.id);
        document.setPrivateText(this.privateText);
        document.setName(this.tittle);
        document.setCover(this.cover);
        document.setSynopsis(synopsis);

        return document;
    }
}
