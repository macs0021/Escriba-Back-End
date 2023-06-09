package com.marco.appEscritura.entity;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.dto.ReadingDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Genre> genres;

    int rating;

    @ManyToMany(mappedBy = "savedDocuments")
    List<User> savedBy;

    @OneToMany(mappedBy = "postedIn", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<Comment> reviews;
    @ManyToOne
    User creator;

    @OneToMany(mappedBy = "beingRead", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<Reading> beingRead;

    public Document() {
        text = "";
        cover = " ";
        synopsis = " ";
        savedBy = new ArrayList<>();
        reviews = new ArrayList<>();
        genres = new ArrayList<>();
        isPublic = false;
        rating = 0;
        beingRead = Collections.EMPTY_LIST;
    }

    public DocumentDTO toDto() {
        List<String> savedUsersUUID = savedBy.stream().map(user -> user.username).collect(Collectors.toList());
        List<ReadingDTO> readingsDto = beingRead.stream().map(reading -> reading.toDto()).collect(Collectors.toList());
        System.out.println("MOSTRANDO EL PRIMERO: " + genres.get(0).getGenre());
        return new DocumentDTO(id, tittle, cover, text, creator.getUsername(), synopsis, genres, savedUsersUUID, readingsDto,isPublic,rating);
    }
}
