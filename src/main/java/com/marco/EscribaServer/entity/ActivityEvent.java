package com.marco.EscribaServer.entity;

import com.marco.EscribaServer.Utils.EntityType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
public class ActivityEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String action;
    private EntityType entityType;
    private String entityId;
    private LocalDateTime timestamp;

    public ActivityEvent() {

    }
}