package com.marco.appEscritura.exceptions.Document;

public class NotExistingDocument extends RuntimeException {
    public NotExistingDocument(String message) {
        super(message);
    }
}
