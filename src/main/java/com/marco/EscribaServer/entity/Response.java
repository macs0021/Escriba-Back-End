package com.marco.EscribaServer.entity;

import com.marco.EscribaServer.Utils.CommentType;
import com.marco.EscribaServer.dto.CommentDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("response")
public class Response extends Comment {
    @NotNull
    @ManyToOne
    private Review review;

    public Response() {
        super();
    }

    public Response(String text, User user, Document document, Review review) {
        super(text, user, document);
        this.review = review;
    }

    @Override
    public CommentDTO toDto() {
        CommentDTO response = super.toDto();
        response.setResponding(this.getReview().getId());
        response.setCommentType(CommentType.RESPONSE);


        return response;
    }
}