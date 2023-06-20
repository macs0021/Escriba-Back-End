package com.marco.appEscritura.entity;

import com.marco.appEscritura.dto.DocumentDTO;
import com.marco.appEscritura.dto.ReadingDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
public class Document implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    @Column(columnDefinition = "TEXT")
    String text;
    @NotBlank(message = "El título no puede estar vacío")
    String tittle;
    boolean isPublic;
    @Lob
    @Column(columnDefinition = "TEXT")
    String synopsis;

    @Lob
    @Column(columnDefinition = "TEXT")
    String cover;

    @Min(value = 0, message = "La calificación no puede ser negativa")
    @Max(value = 5, message = "La calificación no puede ser mayor a 5")
    int rating;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Genre> genres;
    @ManyToMany(mappedBy = "savedDocuments")
    List<User> savedBy;
    @OneToMany(mappedBy = "postedIn", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<Comment> reviews;
    @NotNull(message = "El creador no puede ser nulo")
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
        return new DocumentDTO(id, tittle, cover, text, creator.getUsername(), synopsis, genres, savedUsersUUID, readingsDto,isPublic,rating);
    }
}
