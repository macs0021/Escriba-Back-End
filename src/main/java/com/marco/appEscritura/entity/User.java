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

    @OneToMany(mappedBy="creator")
    List<Document> created;

    @ManyToMany
    List<Document> savedDocuments;

    @OneToMany(mappedBy = "postedBy")
    List<Comment> postedComments;

    @OneToMany(mappedBy = "reader")
    List<Reading> reading;

    public User() {
        username = "";
        password = "";
        savedDocuments = new ArrayList<>();
        postedComments = new ArrayList<>();
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    UserDTO toDto(){
        return new UserDTO();
    }
}
