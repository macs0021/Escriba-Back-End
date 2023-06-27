package com.marco.EscribaServer.entity;

import com.marco.EscribaServer.dto.ReadingDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public ReadingDTO toDto(){
        return new ReadingDTO(reader.username, beingRead.getId(), readingSpot);
    }
}
