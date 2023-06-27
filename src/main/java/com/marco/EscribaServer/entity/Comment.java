package com.marco.EscribaServer.entity;

import com.marco.EscribaServer.dto.CommentDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "commentType", discriminatorType = DiscriminatorType.STRING)
@Data
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Lob
    private String text;

    @ManyToOne
    private User postedBy;
    @Column(nullable = false)
    private LocalDateTime postedAt;

    @ManyToOne
    Document postedIn;

    public Comment() {
        this.postedAt = LocalDateTime.now();
    }

    public Comment(String text, User user, Document postedIn) {
        this.text = text;
        this.postedBy = user;
        this.postedAt = LocalDateTime.now();
        this.postedIn = postedIn;
    }

    public CommentDTO toDto(){
        return new CommentDTO(id,text,postedBy.username,postedAt,postedIn.getId());
    }
}
