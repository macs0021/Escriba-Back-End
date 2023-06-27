package com.marco.EscribaServer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
