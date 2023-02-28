package com.marco.appEscritura.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@IdClass(ReadingID.class)
public class Reading implements Serializable {
    @Id
    @ManyToOne
    User reader;
    @Id
    @ManyToOne
    Document beingRead;

    float readingSpot;
}
