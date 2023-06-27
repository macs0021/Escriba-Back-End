package com.marco.EscribaServer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RegisterDto {
    @NotBlank(message = "Username cannot be empty.")
    @Size(min = 3, message = "Username must be at least 3 characters long.")
    private String username;

    @NotBlank(message = "Email address cannot be empty.")
    @Email(message = "Email address must be valid.")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 5, message = "Password must be at least 5 characters long.")
    private String password;
    private String profileImage;
}
