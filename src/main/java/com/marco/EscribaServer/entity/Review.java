package com.marco.EscribaServer.entity;

import com.marco.EscribaServer.Utils.CommentType;
import com.marco.EscribaServer.dto.CommentDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("review")
@Data
public class Review extends Comment {
    private int rating;

    public Review() {
        super();
    }
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Response> responses = new ArrayList<>();

    public Review(String text, User user, Document document, int rating) {
        super(text, user, document);
        this.rating = rating;
    }

    @Override
    public CommentDTO toDto() {
        CommentDTO commentDTO = super.toDto();
        commentDTO.setCommentType(CommentType.REVIEW);
        commentDTO.setRating(rating);

        return commentDTO;
    }

}