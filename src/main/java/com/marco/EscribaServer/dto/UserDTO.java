package com.marco.EscribaServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDTO {
    UUID id;
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
