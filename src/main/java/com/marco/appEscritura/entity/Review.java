package com.marco.appEscritura.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Review extends Comment {
    @Column(nullable = false)
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
}