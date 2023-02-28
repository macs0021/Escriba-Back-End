package com.marco.appEscritura.entity;

import com.marco.appEscritura.dto.DocumentDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@Table(name="document")
public class Document  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    String publicText;
    @Column(columnDefinition="TEXT", length = 10000)
    String privateText;

    String name;

    String synopsis;

    @Column(length = 10000000)
    String cover;
    List<String> genres;

    List<String> tags;

    @ManyToMany(mappedBy = "likedDocuments")
    List<User> likedBy;

    @OneToMany(mappedBy = "commentedDocument")
    List<Comment> comments;
    @ManyToOne
    User creator;

    public Document() {
        privateText = " ";
        publicText = " ";
        cover = " ";
        synopsis = " ";
        likedBy = new ArrayList<>();
        comments = new ArrayList<>();
        genres = new ArrayList<>();
        tags = new ArrayList<>();
    }

    public DocumentDTO toDto(){
     return new DocumentDTO(id,name,cover,synopsis,privateText);
    }
}
