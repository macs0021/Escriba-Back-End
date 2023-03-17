package com.marco.appEscritura.entity;

import com.marco.appEscritura.dto.DocumentDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name="document")
public class Document  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    String publicText;
    @Column(columnDefinition="TEXT", length = 10000)
    String privateText;

    String tittle;

    @Column(length = 10000  )
    String synopsis;

    @Column(length = 10000000)
    String cover;
    List<String> genres;

    List<String> tags;

    @ManyToMany(mappedBy = "savedDocuments")
    List<User> savedBy;

    @OneToMany(mappedBy = "commentedDocument")
    List<Comment> comments;
    @ManyToOne
    User creator;

    public Document() {
        privateText = " ";
        publicText = " ";
        cover = " ";
        synopsis = " ";
        savedBy = new ArrayList<>();
        comments = new ArrayList<>();
        genres = new ArrayList<>();
        tags = new ArrayList<>();
    }

    public DocumentDTO toDto(){
        List<UUID> savedUsersUUID = savedBy.stream().map(user -> user.id).collect(Collectors.toList());
        return new DocumentDTO(id, tittle, cover, privateText, creator.getUsername(), synopsis,genres,savedUsersUUID);
    }
}
