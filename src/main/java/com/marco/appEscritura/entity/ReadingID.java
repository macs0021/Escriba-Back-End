package com.marco.appEscritura.entity;

import java.io.Serial;
import java.io.Serializable;

public class ReadingID implements Serializable {
    User reader;
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
