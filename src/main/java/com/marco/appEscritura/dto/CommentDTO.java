package com.marco.appEscritura.dto;

import com.marco.appEscritura.Utils.CommentType;
import com.marco.appEscritura.entity.Document;
import com.marco.appEscritura.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String text;
    private String postedBy;
    private LocalDateTime postedAt;
    private long postedIn;
    private CommentType commentType;
    private int rating;
    private Long responding;

    public CommentDTO(Long id, String text, String postedBy, LocalDateTime postedAt, long postedIn) {
        this.id = id;
        this.text = text;
        this.postedBy = postedBy;
        this.postedAt = postedAt;
        this.postedIn = postedIn;
    }
}
