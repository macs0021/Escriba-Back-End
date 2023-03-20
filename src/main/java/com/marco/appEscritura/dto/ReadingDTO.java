package com.marco.appEscritura.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadingDTO {
    String username;
    Long document;
    float readingSpot;
}
