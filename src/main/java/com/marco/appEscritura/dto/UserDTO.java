package com.marco.appEscritura.dto;

import com.marco.appEscritura.entity.Comment;
import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.entity.Reading;
import com.marco.appEscritura.entity.User;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDTO {
    String name;
    String image;
    String email;
    String description;
    List<String> following;
    List<String> followers;

    public UserDTO() {
        this.name = "";
        this.image = "";
        this.email = "";
        this.description = "";
    }

}
