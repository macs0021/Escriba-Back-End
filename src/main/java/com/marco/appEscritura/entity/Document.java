package com.marco.appEscritura.entity;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.dto.ReadingDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "document")
public class Document implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    String publicText;
    @Column(columnDefinition = "TEXT", length = 10000)
    String privateText;

    String tittle;

    @Lob
    @Column(columnDefinition = "TEXT")
    String synopsis;

    @Lob
    @Column(columnDefinition = "TEXT")
    String cover;
    List<String> genres;

    List<String> tags;

    @ManyToMany(mappedBy = "savedDocuments")
    List<User> savedBy;

    @OneToMany(mappedBy = "postedIn")
    List<Review> reviews;
    @ManyToOne
    User creator;

    @OneToMany(mappedBy = "beingRead")
    List<Reading> beingRead;

    public Document() {
        privateText = " ";
        publicText = " ";
        cover = " ";
        synopsis = " ";
        savedBy = new ArrayList<>();
        reviews = new ArrayList<>();
        genres = new ArrayList<>();
        tags = new ArrayList<>();
    }

    public DocumentDTO toDto() {
        List<String> savedUsersUUID = savedBy.stream().map(user -> user.username).collect(Collectors.toList());
        List<ReadingDTO> readingsDto = beingRead.stream().map(reading -> reading.toDto()).collect(Collectors.toList());
        return new DocumentDTO(id, tittle, cover, privateText, creator.getUsername(), synopsis, genres, savedUsersUUID, readingsDto);
    }
}
