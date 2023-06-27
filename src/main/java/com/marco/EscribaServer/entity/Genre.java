package com.marco.EscribaServer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    @NotNull
    @Column(unique = true)
    String genre;

    public Genre(String genre) {
        this.genre = genre;
        this.documentsOfGenre = new ArrayList<>();
    }
    @ManyToMany(mappedBy = "genres")
    List<Document> documentsOfGenre;


}
