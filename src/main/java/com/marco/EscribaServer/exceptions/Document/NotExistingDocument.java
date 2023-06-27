package com.marco.EscribaServer.exceptions.Document;

public class NotExistingDocument extends RuntimeException {
    public NotExistingDocument(String message) {
        super(message);
    }
}
