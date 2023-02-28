package com.marco.appEscritura.exceptions;

public class NotExistingDocument extends RuntimeException {
    public NotExistingDocument(String message) {
        super(message);
    }
}
