package com.marco.appEscritura.security.dto;

import lombok.NoArgsConstructor;

public class JwtDto {
    private String token;

    public JwtDto() {
    }

    public JwtDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}