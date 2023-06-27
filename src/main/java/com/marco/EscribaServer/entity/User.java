package com.marco.EscribaServer.entity;

import com.marco.EscribaServer.dto.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Column(unique = true)
    String username;
    @NotBlank
    String password;
    String email;
    @Lob
    @Column(columnDefinition = "TEXT")
    String image;
    @Lob
    @Column(columnDefinition = "TEXT")
    String description;
    @OneToMany(mappedBy = "creator")
    List<Document> created;
    @ManyToMany
    List<Document> savedDocuments;
    @OneToMany(mappedBy = "postedBy")
    List<Comment> postedComments;
    @OneToMany(mappedBy = "reader")
    List<Reading> reading;

    @ManyToMany
    List<User> following;

    @ManyToMany(mappedBy = "following")
    List<User> followers;

    public User() {
        username = "";
        password = "";
        image = "";
        description = "";
        savedDocuments = new ArrayList<>();
        postedComments = new ArrayList<>();
        followers = new ArrayList<>();
        following = new ArrayList<>();
        email = "";
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.image = "";
        this.description = "";
        this.savedDocuments = new ArrayList<>();
        this.postedComments = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
    }

    public UserDTO toDto() {
        UserDTO user = new UserDTO();
        user.setId(id);
        user.setName(username);
        user.setImage(image);
        user.setDescription(description);
        user.setFollowers(followers.stream()
                .map(User::getUsername)
                .collect(Collectors.toList()));
        user.setFollowing(following.stream()
                .map(User::getUsername)
                .collect(Collectors.toList()));
        user.setEmail(email);
        return user;
    }
}
