package com.marco.appEscritura.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDTO {

    UUID id;
    String name;

    List<Long> created;
    List<Long> savedDocuments;

    public UserDTO() {

    }

}
