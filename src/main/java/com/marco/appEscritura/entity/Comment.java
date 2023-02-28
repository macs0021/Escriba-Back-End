package com.marco.appEscritura.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    String content;
    @ManyToOne
    Document commentedDocument;
    @ManyToOne
    User postedBy;
    @OneToMany
    List<Comment> responses;
    @ManyToOne
    Comment respondingTo;
    public Comment() {
        responses = new ArrayList<>();
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

   public Document getCommentedDocument() {
        return commentedDocument;
    }

    public void setCommentedDocument(Document commentedDocument) {
        this.commentedDocument = commentedDocument;
    }

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }
}
