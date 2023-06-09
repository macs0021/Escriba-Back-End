package com.marco.appEscritura.entity;

import com.marco.appEscritura.Utils.EntityType;
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
    private String username;//Username del usuario que hace la acción
    private String action;//Acción que se realiza, para documento podría ser "ha publicado" o "ha comenzado a leer"
    private EntityType entityType;//Tipo de entidad, enum que contiene DOCUMENT, REVIEW, USER...
    private String entityId;//ID de la entidad a la que se hace referencia, dependiendo del EntityType se le hará un Parse de String a otro tipo de dato
    private LocalDateTime timestamp;//Momento en el que se ha realizado la acción

    public ActivityEvent() {

    }
}