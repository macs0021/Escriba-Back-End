package com.marco.appEscritura.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Response extends Comment {
    @ManyToOne
    private Review review;

    public Response() {
        super();
    }

    public Response(String text, User user, Document document, Review review) {
        super(text, user, document);
        this.review = review;
    }
}
