package com.marco.appEscritura.entity;

import com.marco.appEscritura.dto.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @NotNull
    @NotBlank
    @Column(unique=true)
    String username;

    @NotBlank
    String password;

    String email;

    @ManyToMany
    List<Document> likedDocuments;

    @OneToMany(mappedBy = "postedBy")
    List<Comment> postedComments;

    public User() {
        username = "";
        password = "";
        likedDocuments = new ArrayList<>();
        postedComments = new ArrayList<>();
    }

    UserDTO toDto(){
        return new UserDTO(this.id, this.username);
    }
}
