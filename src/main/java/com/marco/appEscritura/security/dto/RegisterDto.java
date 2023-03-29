package com.marco.appEscritura.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RegisterDto {
    @NotBlank
    private String username;

    private String email;
    @NotBlank
    private String password;

    private String profileImage;
}
