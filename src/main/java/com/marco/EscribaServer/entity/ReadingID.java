package com.marco.EscribaServer.entity;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class ReadingID implements Serializable {
    @NotNull
    User reader;
    @NotNull
    Document beingRead;

    public ReadingID() { }

    public ReadingID(User reader, Document beingRead) {
        this.reader = reader;
        this.beingRead = beingRead;
    }

    public User getReader() {
        return reader;
    }

    public void setReader(User reader) {
        this.reader = reader;
    }

    public Document getBeingRead() {
        return beingRead;
    }

    public void setBeingRead(Document beingRead) {
        this.beingRead = beingRead;
    }
}
