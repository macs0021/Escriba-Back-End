package com.marco.appEscritura.entity;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.dto.ReadingDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "document")
public class Document implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(columnDefinition = "TEXT", length = 10000)
    String text;

    String tittle;

    boolean isPublic;

    @Lob
    @Column(columnDefinition = "TEXT")
    String synopsis;

    @Lob
    @Column(columnDefinition = "TEXT")
    String cover;
    @ElementCollection
    @CollectionTable(name = "document_genres", joinColumns = @JoinColumn(name = "document_id"))
    @Column(name = "genre")
    List<String> genres;

    List<String> tags;

    int rating;

    @ManyToMany(mappedBy = "savedDocuments")
    List<User> savedBy;

    @OneToMany(mappedBy = "postedIn")
    List<Review> reviews;
    @ManyToOne
    User creator;

    @OneToMany(mappedBy = "beingRead")
    List<Reading> beingRead;

    public Document() {
        text = "";
        cover = " ";
        synopsis = " ";
        savedBy = new ArrayList<>();
        reviews = new ArrayList<>();
        genres = new ArrayList<>();
        tags = new ArrayList<>();
        isPublic = false;
        rating = 0;
        beingRead = Collections.EMPTY_LIST;
    }

    public DocumentDTO toDto() {
        List<String> savedUsersUUID = savedBy.stream().map(user -> user.username).collect(Collectors.toList());
        List<ReadingDTO> readingsDto = beingRead.stream().map(reading -> reading.toDto()).collect(Collectors.toList());
        return new DocumentDTO(id, tittle, cover, text, creator.getUsername(), synopsis, genres, savedUsersUUID, readingsDto,isPublic,rating);
    }
}
